package com.vcco.hiltoncodechallenge.ui.viewmodel

import android.content.Context
import android.net.InetAddresses
import android.os.Build
import android.util.Log
import android.util.Patterns
import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vcco.hiltoncodechallenge.R
import com.vcco.hiltoncodechallenge.database.dao.DomainInfoDao
import com.vcco.hiltoncodechallenge.network.SearchIPService
import com.vcco.hiltoncodechallenge.ui.uistate.DomainInfoUiState
import com.vcco.hiltoncodechallenge.ui.util.IpSearchMapper
import com.vcco.hiltoncodechallenge.ui.util.IpValidator
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ipInfoService: SearchIPService,
    private val db: DomainInfoDao
) : ViewModel() {

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val _uiState = MutableStateFlow(DomainInfoUiState())
    val uiState: StateFlow<DomainInfoUiState> = _uiState.asStateFlow()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal val _infoSearch = MutableStateFlow("")
    val infoSearch: StateFlow<String> = _infoSearch.asStateFlow()

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    var dispatcher = Dispatchers.IO

    fun updateInfoSearch(infoSearch: String) {
        _infoSearch.update { infoSearch }
    }

    fun getIpInfo() {
        if (!isValidIP()) {
            updateCurrentState(
                DomainInfoUiState(
                    error = context.getString(R.string.invalid_ip)
                )
            )
            return
        }


        viewModelScope.launch(dispatcher) {
            try {
                updateCurrentState(DomainInfoUiState(isLoading = true))
                val dbResponse = db.getIpInfoFromIp(_infoSearch.value)
                Log.i("VM", "Db response $dbResponse")
                dbResponse?.let { info ->
                    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                    val dateSearched = LocalDateTime.parse(info.timestamp, formatter)
                    val fiveMinutesAgo = LocalDateTime.now().minusMinutes(5)
                    Log.i("VM", "The time of search is $dateSearched and $fiveMinutesAgo")
                    if (!dateSearched.isBefore(fiveMinutesAgo)) {
                        Log.i("VM", "The search is valid, returning the job")
                        updateCurrentState(
                            DomainInfoUiState(
                                domainInfo = info,
                                isLoading = false
                            )
                        )
                        return@launch
                    }
                }
                val response = ipInfoService.getIpInfo(_infoSearch.value)
                response.message?.let {
                    updateCurrentState(
                        DomainInfoUiState(
                            error = it
                        )
                    )
                    return@launch
                }
                val mappedResult = IpSearchMapper.map(response)
                updateCurrentState(
                    DomainInfoUiState(
                        domainInfo = mappedResult
                    )
                )
                if (dbResponse == null)
                    db.InsertIpInfo(mappedResult)
                else
                    db.updateIpInfo(mappedResult)

            } catch (e: Exception) {
                e.printStackTrace()
                updateCurrentState(
                    DomainInfoUiState(
                        error = e.message
                    )
                )
            }
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun isValidIP() =
        IpValidator.IpValidator.isValidIp(_infoSearch.value)

    private fun updateCurrentState(infoUiState: DomainInfoUiState) {
        _uiState.update { currentState ->
            currentState.copy(
                domainInfo = infoUiState.domainInfo,
                isLoading = infoUiState.isLoading,
                error = infoUiState.error
            )
        }
    }
}