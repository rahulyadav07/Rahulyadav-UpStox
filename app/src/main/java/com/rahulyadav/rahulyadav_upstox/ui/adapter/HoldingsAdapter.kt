package com.rahulyadav.rahulyadav_upstox.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rahulyadav.rahulyadav_upstox.data.model.Stocks
import com.rahulyadav.rahulyadav_upstox.databinding.ItemHoldingBinding

import com.rahulyadav.rahulyadav_upstox.utils.DisplayFormatter

/**
 * RecyclerView adapter for displaying holdings list
 * Follows Single Responsibility Principle - only handles UI binding
 * Uses View Binding for type safety and better performance
 */
class HoldingsAdapter(
    private val context: Context,
    private var stocks: List<Stocks> = emptyList(),
    private val onItemClickListener: ((Stocks) -> Unit)? = null
) : RecyclerView.Adapter<HoldingsAdapter.HoldingViewHolder>() {

    /**
     * Update the holdings list and notify adapter
     */
    fun updateHoldings(newStocks: List<Stocks>) {
        stocks = newStocks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HoldingViewHolder {
        val binding = ItemHoldingBinding.inflate(
            LayoutInflater.from(parent.context), 
            parent, 
            false
        )
        return HoldingViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HoldingViewHolder, position: Int) {
        holder.bind(stocks[position])
    }

    override fun getItemCount(): Int = stocks.size


    inner class HoldingViewHolder(
        private val binding: ItemHoldingBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(stocks: Stocks) {
            // Set symbol
            binding.textSymbol.text = stocks.symbol

            // Set quantity using centralized formatter
            binding.textQuantity.text = DisplayFormatter.formatQuantity(stocks.quantity)

            // Set LTP using centralized formatter
            binding.textLtp.text = DisplayFormatter.formatCurrency(stocks.ltp)

            // Set P&L using centralized formatter
            val pnl = stocks.getPnL()
            binding.textPnL.text = DisplayFormatter.formatCurrency(pnl)

            // Set P&L color using centralized formatter
            val pnlColorResource = DisplayFormatter.getPnLColorResource(pnl)
            binding.textPnL.setTextColor(context.getColor(pnlColorResource))
            
            // Set click listener
            binding.root.setOnClickListener {
                onItemClickListener?.invoke(stocks)
            }
        }
    }
}
