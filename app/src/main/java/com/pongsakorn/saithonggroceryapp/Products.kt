package com.pongsakorn.saithonggroceryapp

import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.pongsakorn.saithonggroceryapp.databinding.ProductsBinding

/**
 * คลาส Products: จัดการตรรกะ (Logic) ของหน้าแสดงรายการสินค้าด้วย RecyclerView ประสิทธิภาพสูง
 */
class Products(
    mainView: View, 
    private val onAddClick: () -> Unit,
    private val onEditClick: (Product) -> Unit,
    private val onProductListChanged: () -> Unit = {},
) {

    private val binding = ProductsBinding.bind(mainView)
    private lateinit var adapter: ProductAdapter
    private var lastDeletedProduct: Product? = null
    private var lastDeletedIndex: Int = 0

    private val deletionModule = DeleteFunction(
        mainView = mainView,
        onDeleteConfirmed = {
            lastDeletedProduct?.let { product ->
                lastDeletedIndex = ProductData.products.indexOf(product)
                ProductData.deleteProduct(product)
                ActivityData.logAction("ลบสินค้าออกจากระบบ", product.name, LogType.DELETE)
                onProductListChanged()
            }
        },
        onUndoClicked = {
            lastDeletedProduct?.let { product ->
                ProductData.restoreProduct(product, lastDeletedIndex)
                onProductListChanged()
            }
        },
        onRefreshList = { refreshProductList() },
    )

    private val tags by lazy {
        listOf(
            binding.tagAll, 
            binding.tagMama, 
            binding.tagDrink, 
            binding.tagSnack,
            binding.tagMilk,
            binding.tagFrozen,
        )
    }

    init {
        setupRecyclerView()
        setupListeners()
    }

    private fun setupRecyclerView() {
        adapter = ProductAdapter(
            onEditClick = { product -> onEditClick(product) },
            onDeleteClick = { product, _ ->
                lastDeletedProduct = product
                deletionModule.showModernDeleteDialog(
                    productName = product.name,
                    productCategory = product.category,
                )
            },
        )
        binding.rvProducts.layoutManager = LinearLayoutManager(binding.root.context)
        binding.rvProducts.adapter = adapter
        refreshProductList()
    }

    private fun setupListeners() {
        tags.forEach { tag ->
            tag.setOnClickListener { updateTags(tag) }
        }

        binding.btnOpenAdd.setOnClickListener {
            onAddClick()
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                refreshProductList()
            }
            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun getFilteredList(): List<Product> {
        val query = binding.etSearch.text.toString().trim()
        val currentCategory = tags.find { (it.typeface != null) && it.typeface.isBold }?.text?.toString() ?: "ทั้งหมด"

        return ProductData.products.filter { product ->
            val matchesSearch = product.name.contains(query, ignoreCase = true)
            val matchesCategory = (currentCategory == "ทั้งหมด") || (product.category == currentCategory)
            matchesSearch && matchesCategory
        }
    }

    fun refreshProductList() {
        adapter.submitList(getFilteredList().map { it.copy() })
    }

    private fun updateTags(selectedTag: TextView) {
        tags.forEach { tag ->
            val isSelected = tag == selectedTag
            tag.setBackgroundResource(if (isSelected) R.drawable.bg_tag_orange else R.drawable.bg_rounded_white)
            tag.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        }
        refreshProductList()
    }
}
