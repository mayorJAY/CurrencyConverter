package com.josycom.mayorjay.currencyconverter.common.data.repositoryImplementation

import com.josycom.mayorjay.currencyconverter.common.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

inline fun <ResultType, RequestType> networkBoundResource(
    crossinline query: () -> Flow<ResultType>,
    crossinline shouldFetch: (ResultType?) -> Boolean = { true },
    crossinline fetch: suspend () -> RequestType,
    crossinline saveFetchResult: suspend (RequestType) -> Unit,
    crossinline onFetchFailed: (Throwable) -> Unit = { },
    dispatcher: CoroutineDispatcher,
) = flow {

    val data = query().firstOrNull()
    emit(Resource.Loading(data))

    val flow = when {
        shouldFetch(data) -> {
            emit(Resource.Loading(data))

            try {
                saveFetchResult(fetch())
                query().map { Resource.Success(it) }
            } catch (throwable: Throwable) {
                onFetchFailed(throwable)
                query().map { Resource.Error(throwable, it) }
            }
        }
        else -> {
            query().map { Resource.Success(it) }
        }
    }
    emitAll(flow)
}.flowOn(dispatcher)