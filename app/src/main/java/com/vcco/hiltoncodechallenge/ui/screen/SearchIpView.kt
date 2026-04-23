package com.vcco.hiltoncodechallenge.ui.screen

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vcco.hiltoncodechallenge.R
import com.vcco.hiltoncodechallenge.database.model.DomainInfo
import com.vcco.hiltoncodechallenge.ui.theme.HiltonCodeChallengeTheme
import com.vcco.hiltoncodechallenge.ui.viewmodel.MainActivityViewModel

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SearchDomainInfoView(
    modifier: Modifier = Modifier
) {
    val vm: MainActivityViewModel = viewModel()
    val ipUIState = vm.uiState.collectAsState().value
    val infoSearch = vm.infoSearch.collectAsState().value
    val keyboardController = LocalSoftwareKeyboardController.current

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
    ) {

        if (ipUIState.isLoading) {
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier
                    .width(64.dp)
            )
        }
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            DomainInfoSearchBoxView(
                infoSearch = infoSearch,
                onUpdateInfoSearch = {
                    vm.updateInfoSearch(it)
                },
                onSearchInfoClick = {
                    vm.getIpInfo()
                    keyboardController?.hide()
                }
            )
            ipUIState.domainInfo?.let {
                Text(text = stringResource(R.string.result))
                SearchDomainInfoResult(it)
                Text(
                    text = stringResource(
                        R.string.result_time_query,
                        it.timestamp
                    )
                )
            }
            ipUIState.error?.let {
                Text(
                    text = stringResource(
                        R.string.search_error,
                        it
                    )
                )
            }
        }
    }
}

@Composable
fun DomainInfoSearchBoxView(
    infoSearch: String,
    onUpdateInfoSearch: (String) -> Unit,
    onSearchInfoClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .wrapContentHeight()
    ) {
        OutlinedTextField(
            value = infoSearch,
            onValueChange = {
                onUpdateInfoSearch(it)
            },
            label = {
                Text(stringResource(R.string.search_ip))
            },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Uri,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    onSearchInfoClick()
                }
            ),
            modifier = Modifier.padding(vertical = 10.dp)
        )

        Button(
            onClick = {
                onSearchInfoClick()
            },
            modifier = Modifier.padding(vertical = 10.dp)
        ) {
            Text(
                stringResource(R.string.search_button)
            )
        }

    }
}

@Composable
fun SearchDomainInfoResult(
    domainInfo: DomainInfo,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .wrapContentHeight()
    ) {
        Row {
            Text(
                text = stringResource(R.string.result_city)
            )
            Text(
                text = domainInfo.city
            )
        }
        Row {
            Text(
                text = stringResource(R.string.result_region_name)
            )
            Text(
                text = "${domainInfo.regionName} (${domainInfo.region})"
            )
        }
        Row {
            Text(
                text = stringResource(R.string.country_result)
            )
            Text(
                text = domainInfo.country
            )
        }
        Row {
            Text(
                text = stringResource(R.string.result_latitude)
            )
            Text(
                text = domainInfo.latitude.toString()
            )
        }
        Row {
            Text(
                text = stringResource(R.string.result_longitude)
            )
            Text(
                text = domainInfo.longitude.toString()
            )
        }
        Row {
            Text(
                text = stringResource(R.string.result_isp)
            )
            Text(
                text = domainInfo.isp
            )
        }
        Row {
            Text(
                text = stringResource(R.string.result_organization)
            )
            Text(
                text = domainInfo.organization
            )
        }
        Row {
            Text(
                text = stringResource(R.string.result_query)
            )
            Text(
                text = domainInfo.query
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchDomainInfoViewPreview() {
    HiltonCodeChallengeTheme {
        SearchDomainInfoView()
    }
}