package com.rahulyadav.rahulyadav_upstox.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.rahulyadav.rahulyadav_upstox.data.model.PortfolioSummary
import com.rahulyadav.rahulyadav_upstox.data.model.Stocks
import com.rahulyadav.rahulyadav_upstox.data.repository.PortfolioRepository

import com.rahulyadav.upstoxassignment.ui.viewmodel.TestablePortfolioViewModel
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PortfolioViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: TestablePortfolioViewModel
    private lateinit var mockRepository: PortfolioRepository
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockRepository = mockk()
        viewModel = TestablePortfolioViewModel(mockRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadUserHoldings sets loading state correctly`() = runTest {
        // Given
        val mockStocks = listOf(
            Stocks("MAHABANK", 990, 38.05, 35.0, 40.0),
            Stocks("ICICI", 100, 118.25, 110.0, 105.0)
        )
        coEvery { mockRepository.getUserHoldings() } returns Result.success(mockStocks)

        val loadingObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isLoading.observeForever(loadingObserver)

        
        viewModel.loadUserHoldings()
        testDispatcher.scheduler.advanceUntilIdle()

        
        verify { loadingObserver.onChanged(true) }
        verify { loadingObserver.onChanged(false) }
    }

    @Test
    fun `loadUserHoldings updates holdings when successful`() = runTest {
        // Given
        val mockStocks = listOf(
            Stocks("MAHABANK", 990, 38.05, 35.0, 40.0),
            Stocks("ICICI", 100, 118.25, 110.0, 105.0)
        )
        coEvery { mockRepository.getUserHoldings() } returns Result.success(mockStocks)

        val holdingsObserver = mockk<Observer<List<Stocks>>>(relaxed = true)
        viewModel.holdings.observeForever(holdingsObserver)

        
        viewModel.loadUserHoldings()
        testDispatcher.scheduler.advanceUntilIdle()

        
        verify { holdingsObserver.onChanged(mockStocks) }
    }

    @Test
    fun `loadUserHoldings updates portfolio summary when successful`() = runTest {

        val mockStocks = listOf(
            Stocks("MAHABANK", 990, 38.05, 35.0, 40.0),
            Stocks("ICICI", 100, 118.25, 110.0, 105.0)
        )
        coEvery { mockRepository.getUserHoldings() } returns Result.success(mockStocks)

        val summaryObserver = mockk<Observer<PortfolioSummary?>>(relaxed = true)
        viewModel.portfolioSummary.observeForever(summaryObserver)


        viewModel.loadUserHoldings()
        testDispatcher.scheduler.advanceUntilIdle()

        
        verify { summaryObserver.onChanged(any()) }
    }

    @Test
    fun `loadUserHoldings sets error when repository fails`() = runTest {
        // Given
        val errorMessage = "Network error"
        coEvery { mockRepository.getUserHoldings() } returns Result.failure(Exception(errorMessage))

        val errorObserver = mockk<Observer<String?>>(relaxed = true)
        viewModel.error.observeForever(errorObserver)

        
        viewModel.loadUserHoldings()
        testDispatcher.scheduler.advanceUntilIdle() // Wait for coroutines to complete

        
        verify { errorObserver.onChanged(errorMessage) }
    }

    @Test
    fun `toggleSummaryExpanded toggles expansion state`() {
        // Given
        val expansionObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isSummaryExpanded.observeForever(expansionObserver)

        
        viewModel.toggleSummaryExpanded()

        
        verify { expansionObserver.onChanged(true) }

        
        viewModel.toggleSummaryExpanded()

        
        verify { expansionObserver.onChanged(false) }
    }

    @Test
    fun `clearError clears error message`() {
        // Given
        val errorObserver = mockk<Observer<String?>>(relaxed = true)
        viewModel.error.observeForever(errorObserver)

        
        viewModel.clearError()

        
        verify { errorObserver.onChanged(null) }
    }

    @Test
    fun `refreshHoldings calls loadUserHoldings`() = runTest {
        // Given
        val mockStocks = listOf(Stocks("TEST", 100, 10.0, 9.0, 11.0))
        coEvery { mockRepository.getUserHoldings() } returns Result.success(mockStocks)

        
        viewModel.refreshHoldings()
        testDispatcher.scheduler.advanceUntilIdle() // Wait for coroutines to complete

        
        coVerify { mockRepository.getUserHoldings() }
    }

    @Test
    fun `selectHolding updates selected stocks`() {
        // Given
        val mockStock = Stocks("TEST", 100, 10.0, 9.0, 11.0)
        val selectedObserver = mockk<Observer<Stocks?>>(relaxed = true)
        viewModel.selectedStocks.observeForever(selectedObserver)

        
        viewModel.selectHolding(mockStock)

        
        verify { selectedObserver.onChanged(mockStock) }
    }

    @Test
    fun `clearSelectedHolding clears selected stocks`() {
        // Given
        val mockStock = Stocks("TEST", 100, 10.0, 9.0, 11.0)
        val selectedObserver = mockk<Observer<Stocks?>>(relaxed = true)
        viewModel.selectedStocks.observeForever(selectedObserver)
        viewModel.selectHolding(mockStock) // First select

        
        viewModel.clearSelectedHolding()

        
        verify { selectedObserver.onChanged(null) }
    }
}
