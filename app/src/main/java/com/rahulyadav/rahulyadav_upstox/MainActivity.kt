package com.rahulyadav.rahulyadav_upstox

import android.animation.ValueAnimator
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.rahulyadav.rahulyadav_upstox.data.model.Stocks
import com.rahulyadav.rahulyadav_upstox.data.model.PortfolioSummary
import com.rahulyadav.rahulyadav_upstox.databinding.ActivityMainBinding
import com.rahulyadav.rahulyadav_upstox.di.DependencyContainer
import com.rahulyadav.rahulyadav_upstox.ui.adapter.HoldingsAdapter
import com.rahulyadav.rahulyadav_upstox.ui.viewmodel.PortfolioViewModel
import com.rahulyadav.rahulyadav_upstox.ui.viewmodel.PortfolioViewModelFactory
import com.rahulyadav.rahulyadav_upstox.ui.viewmodel.PortfolioUiState
import com.rahulyadav.rahulyadav_upstox.utils.DisplayFormatter


class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: PortfolioViewModel
    private lateinit var holdingsAdapter: HoldingsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.main) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupViewModel()
        setupRecyclerView()
        setupSwipeRefresh()
        setupPortfolioSummary()
        observeViewModel()
    }
    
    private fun setupViewModel() {
        val repository = DependencyContainer.getPortfolioRepository()
        val factory = PortfolioViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[PortfolioViewModel::class.java]
    }
    
    private fun setupRecyclerView() {
        holdingsAdapter = HoldingsAdapter(this) { holding ->
            viewModel.selectHolding(holding)
        }
        binding.recyclerViewHoldings.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = holdingsAdapter
        }
    }
    
    private fun setupSwipeRefresh() {
        binding.swipeRefreshLayout.setOnRefreshListener {
            viewModel.refreshHoldings()
        }
    }
    
    private fun setupPortfolioSummary() {

        binding.cardView.portfolioSummaryCard.setOnClickListener {
            viewModel.toggleSummaryExpanded()
        }

        binding.cardView.portfolioSummaryCard.setOnLongClickListener {
            val currentState = viewModel.uiState.value
            if (currentState is PortfolioUiState.Success && currentState.selectedStocks != null) {
                viewModel.clearSelectedHolding()
                true
            } else {
                false
            }
        }
    }
    
    private fun observeViewModel() {
        viewModel.uiState.observe(this) { uiState ->
            when (uiState) {
                is PortfolioUiState.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                    binding.swipeRefreshLayout.isRefreshing = true
                    binding.textError.visibility = View.GONE
                    binding.recyclerViewHoldings.visibility = View.GONE
                }
                
                is PortfolioUiState.Success -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.textError.visibility = View.GONE
                    binding.recyclerViewHoldings.visibility = View.VISIBLE
                    
                    // Update holdings
                    holdingsAdapter.updateHoldings(uiState.holdings)
                    
                    // Update portfolio summary or item details
                    if (uiState.selectedStocks != null) {
                        showItemDetails(uiState.selectedStocks)
                    } else {
                        updatePortfolioSummary(uiState.portfolioSummary)
                    }
                    
                    // Handle expand/collapse animation
                    animateSummaryExpansion(uiState.isSummaryExpanded)
                }
                
                is PortfolioUiState.Error -> {
                    binding.progressBar.visibility = View.GONE
                    binding.swipeRefreshLayout.isRefreshing = false
                    binding.textError.text = uiState.message
                    binding.textError.visibility = View.VISIBLE
                    binding.recyclerViewHoldings.visibility = View.GONE
                }
            }
        }
    }
    
    private fun updatePortfolioSummary(summary: PortfolioSummary) {
        binding.cardView.textCurrentValue.text = DisplayFormatter.formatCurrency(summary.currentValue)
        binding.cardView.textTotalInvestment.text = DisplayFormatter.formatCurrency(summary.totalInvestment)
        
        // Format Today's P&L with proper color
        binding.cardView.textTodaysPnL.text = DisplayFormatter.formatCurrency(summary.todaysPnL)
        val todaysPnLColorResource = DisplayFormatter.getPnLColorResource(summary.todaysPnL)
        binding.cardView.textTodaysPnL.setTextColor(getColor(todaysPnLColorResource))

        // Format Total P&L with proper color
        binding.cardView.textTotalPnL.text = DisplayFormatter.formatPnLWithPercentage(
            summary.totalPnL, 
            summary.getTotalPnLPercentage()
        )
        val pnlColorResource = DisplayFormatter.getPnLColorResource(summary.totalPnL)
        binding.cardView.textTotalPnL.setTextColor(getColor(pnlColorResource))
    }
    
    private fun animateSummaryExpansion(isExpanded: Boolean) {
        if (isExpanded) {
            expandItemContainer()
        } else {
            collapseItemContainer()
        }
    }
    
    private fun expandItemContainer() {
        val itemContainer = binding.cardView.itemContainer
        

        itemContainer.measure(
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        
        val targetHeight = itemContainer.measuredHeight
        
        // Set initial height to 0 and make visible
        itemContainer.layoutParams.height = 0
        itemContainer.visibility = View.VISIBLE
        itemContainer.requestLayout()
        
        // Animate to target height
        val animator = ValueAnimator.ofInt(0, targetHeight)
        animator.duration = 300
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation ->
            val height = animation.animatedValue as Int
            itemContainer.layoutParams.height = height
            itemContainer.requestLayout()
        }
        animator.start()
    }
    
    private fun collapseItemContainer() {
        val itemContainer = binding.cardView.itemContainer
        val startHeight = itemContainer.height
        
        val animator = ValueAnimator.ofInt(startHeight, 0)
        animator.duration = 300
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation ->
            val height = animation.animatedValue as Int
            itemContainer.layoutParams.height = height
            itemContainer.requestLayout()
        }
        animator.addListener(object : android.animation.AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: android.animation.Animator) {
                itemContainer.visibility = View.GONE
            }
        })
        animator.start()
    }
    
    private fun showItemDetails(stocks: Stocks) {
        binding.cardView.textCurrentValue.text = DisplayFormatter.formatCurrency(stocks.getCurrentValue())
        binding.cardView.textTotalInvestment.text = DisplayFormatter.formatCurrency(stocks.getTotalInvestment())
        
        // Format Today's P&L with proper color
        val todaysPnL = stocks.getTodaysPnL()
        binding.cardView.textTodaysPnL.text = DisplayFormatter.formatCurrency(todaysPnL)
        val todaysPnLColorResource = DisplayFormatter.getPnLColorResource(todaysPnL)
        binding.cardView.textTodaysPnL.setTextColor(getColor(todaysPnLColorResource))
        
        // Format Total P&L with proper color
        val pnl = stocks.getPnL()
        binding.cardView.textTotalPnL.text = DisplayFormatter.formatPnLWithPercentage(
            pnl, 
            stocks.getPnLPercentage()
        )
        val pnlColorResource = DisplayFormatter.getPnLColorResource(pnl)
        binding.cardView.textTotalPnL.setTextColor(getColor(pnlColorResource))
    }


}