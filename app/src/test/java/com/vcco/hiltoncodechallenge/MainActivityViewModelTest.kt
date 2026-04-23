package com.vcco.hiltoncodechallenge

import android.content.Context
import android.util.Patterns
import com.vcco.hiltoncodechallenge.database.dao.DomainInfoDao
import com.vcco.hiltoncodechallenge.database.model.DomainInfo
import com.vcco.hiltoncodechallenge.model.ApiResponse
import com.vcco.hiltoncodechallenge.network.SearchIPService
import com.vcco.hiltoncodechallenge.ui.viewmodel.MainActivityViewModel
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import okhttp3.internal.wait
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.Mockito.spy
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.regex.Matcher
import java.util.regex.Pattern

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class MainActivityViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()


    @Mock
    lateinit var service: SearchIPService

    @Mock
    lateinit var dao: DomainInfoDao

    @Mock
    lateinit var context: Context

    lateinit var vm: MainActivityViewModel

    //Models response
    lateinit var apiResponseCorrect: ApiResponse
    lateinit var apiResponseIncorrect: ApiResponse
    lateinit var dbResponseCorrect: DomainInfo
    lateinit var oldResponseCorrect: DomainInfo


    @Before
    fun setUp() {

        vm = MainActivityViewModel(context, service, dao)
        apiResponseCorrect = ApiResponse(
            status = "success",
            country = "United States",
            countryCode = "US",
            region = "TX",
            regionName = "Texas",
            city = "Dallas",
            zipCode = "75270",
            latitude = 32.7797,
            longitude = -96.8022,
            timeZone = "America/Chicago",
            isp = "Facebook, Inc.",
            organization = "Meta Platforms Ireland Limited",
            organizationNumber = "AS32934 Facebook, Inc.",
            query = "57.144.200.1"
        )

        apiResponseIncorrect = ApiResponse(
            status = "fail",
            message = "invalid query",
            query = "57.144.200.1"
        )

        dbResponseCorrect = DomainInfo(
            status = "success",
            country = "United States",
            countryCode = "US",
            region = "TX",
            regionName = "Texas",
            city = "Dallas",
            zipCode = "75270",
            latitude = 32.7797,
            longitude = -96.8022,
            timeZone = "America/Chicago",
            isp = "Facebook, Inc.",
            organization = "Meta Platforms Ireland Limited",
            organizationNumber = "AS32934 Facebook, Inc.",
            query = "57.144.200.1",
            timestamp = "2026-04-24 18:30:00"
        )

        oldResponseCorrect = DomainInfo(
            status = "success",
            country = "United States",
            countryCode = "US",
            region = "TX",
            regionName = "Texas",
            city = "Dallas",
            zipCode = "75270",
            latitude = 32.7797,
            longitude = -96.8022,
            timeZone = "America/Chicago",
            isp = "Facebook, Inc.",
            organization = "Meta Platforms Ireland Limited",
            organizationNumber = "AS32934 Facebook, Inc.",
            query = "57.144.200.1",
            timestamp = "2026-04-24 18:00:00"
        )

    }


    @Test
    fun InfoDomainGetInfoFromWenService() = runTest {
        
        val ipToSearch = "57.144.200.1"
        vm.dispatcher = mainDispatcherRule.testDispatcher

        // REEMPLAZA "fetchFromApi" con el método real de tu servicio
        `when`(service.getIpInfo(ipToSearch)).thenReturn(apiResponseCorrect)



        
        vm.updateInfoSearch(ipToSearch)
        
        vm.getIpInfo()
        advanceUntilIdle()

        val uiState = vm.uiState.value

        assertNotNull(uiState.domainInfo)
        assertEquals(dbResponseCorrect.longitude, uiState.domainInfo?.longitude)
        assertEquals(dbResponseCorrect.isp, uiState.domainInfo?.isp)
        assertEquals(dbResponseCorrect.longitude, uiState.domainInfo?.longitude)
        assertEquals(dbResponseCorrect.latitude, uiState.domainInfo?.latitude)
        assertEquals(dbResponseCorrect.longitude, uiState.domainInfo?.longitude)
    }


    @Test
    fun InfoDomainGetInfoFromWenDao() = runTest {
        
        val ipToSearch = "57.144.200.1"
        vm.dispatcher = mainDispatcherRule.testDispatcher

        // REEMPLAZA "fetchFromApi" con el método real de tu servicio
        `when`(dao.getIpInfoFromIp(ipToSearch)).thenReturn(dbResponseCorrect)



        
        vm.updateInfoSearch(ipToSearch)
        
        vm.getIpInfo()
        advanceUntilIdle()

        val uiState = vm.uiState.value

        assertNotNull(uiState.domainInfo)
        assertEquals(dbResponseCorrect, uiState.domainInfo)
    }

    @Test
    fun InfoDomainInvalidIp() = runTest {
        
        val ipToSearch = "57.144.200"
        `when`(context.getString(R.string.invalid_ip)).thenReturn("Invalid Ip Address, please enter a valid ip")

        vm.dispatcher = mainDispatcherRule.testDispatcher

        vm.getIpInfo()
        advanceUntilIdle()

        val uiState = vm.uiState.value

        assertNotNull(uiState.error)
        assertEquals("Invalid Ip Address, please enter a valid ip", uiState.error)
    }


    @Test
    fun InfoDomainInvalidQuery() = runTest {

        val ipToSearch = "57.144.200.99"
        `when`(service.getIpInfo(ipToSearch)).thenReturn(apiResponseIncorrect)

        vm.dispatcher = mainDispatcherRule.testDispatcher
        
        vm.updateInfoSearch(ipToSearch)
 
        vm.getIpInfo()
        advanceUntilIdle()
        
        val uiState = vm.uiState.value

        assertNotNull(uiState.error)
        assertEquals(apiResponseIncorrect.message, uiState.error)
    }

    @Test
    fun InfoDomainOldQueryRetrieveNewQuery() = runTest {

        val ipToSearch = "57.144.200.99"

        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
        val reallyOldTime = LocalDateTime.now().minusMinutes(10).format(formatter)

        
        val testOldResponse = oldResponseCorrect.copy(
            timestamp = reallyOldTime
        )
        `when`(dao.getIpInfoFromIp(ipToSearch)).thenReturn(testOldResponse)
        `when`(service.getIpInfo(ipToSearch)).thenReturn(apiResponseCorrect)

        vm.dispatcher = mainDispatcherRule.testDispatcher

        vm.updateInfoSearch(ipToSearch)

        vm.getIpInfo()
        advanceUntilIdle()

        val uiState = vm.uiState.value

        assertNotNull(uiState.domainInfo)
        assertNotEquals(uiState.domainInfo?.timestamp, oldResponseCorrect.timestamp)
    }
    

}