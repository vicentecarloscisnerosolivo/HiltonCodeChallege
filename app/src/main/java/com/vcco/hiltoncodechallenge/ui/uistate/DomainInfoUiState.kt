package com.vcco.hiltoncodechallenge.ui.uistate

import com.vcco.hiltoncodechallenge.database.model.DomainInfo

data class DomainInfoUiState(
    val domainInfo: DomainInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)