package com.pongsakorn.saithonggroceryapp.ui.products

import android.graphics.Typeface
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import com.pongsakorn.saithonggroceryapp.R
import com.pongsakorn.saithonggroceryapp.data.ActivityData
import com.pongsakorn.saithonggroceryapp.data.ProductRepository
import com.pongsakorn.saithonggroceryapp.databinding.ProductsBinding
import com.pongsakorn.saithonggroceryapp.model.LogType
import com.pongsakorn.saithonggroceryapp.model.Product

/**
 * คลาส Products: จัดการตรรกะ (Logic) ของหน้าแสดงรายการสินค้าด้วย RecyclerView ประสิทธิภาพสูง
 */
class Products(
    mainView: View,
    private val repo: ProductRepository,
    private val onAddClick: () -> Unit,
    private val onEditClick: (Product) -> Unit,
    private val onProductListChanged: () -> Unit = {},
) {

    private val binding = ProductsBinding.bind(mainView)
    private lateinit var adapter: ProductAdapter
    private var lastDeletedProduct: Product? = null
    private var lastDeletedIndex: Int = 0
    private var lastDeleteLogId: String? = null

    // หมวดหมู่ที่เลือกอยู่ (เก็บเป็นตัวแปรตรง ๆ แทนการเดาจากตัวอักษรหนาของแท็บ)
    private val categoryAll = mainView.context.getString(R.string.category_all)
    private var selectedCategory: String = categoryAll

    private val deletionModule = DeleteFunction(
        mainView = mainView,
        onDeleteConfirmed = {
            lastDeletedProduct?.let { product ->
                lastDeletedIndex = repo.indexOf(product.id)
                repo.delete(product)
                lastDeleteLogId = ActivityData.logAction("ลบสินค้าออกจากระบบ", product.name, LogType.DELETE)
                onProductListChanged()
            }
        },
        onUndoClicked = {
            lastDeletedProduct?.let { product ->
                repo.restore(product, lastDeletedIndex)
                lastDeleteLogId?.let { ActivityData.removeLog(it) }
                lastDeleteLogId = null
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
            onDeleteClick = { product ->
                lastDeletedProduct = product
                deletionModule.showModernDeleteDialog(
                    productName = product.name,
                    productCategory = product.category,
                )
            },
        )
        binding.rvProducts.setHasFixedSize(true)
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

        return repo.getAll().filter { product ->
            val matchesSearch = product.name.contains(query, ignoreCase = true)
            val matchesCategory = (selectedCategory == categoryAll) || (product.category == selectedCategory)
            matchesSearch && matchesCategory
        }
    }

    fun refreshProductList() {
        val items = getFilteredList()
        adapter.submitList(items.map { it.copy() })
        binding.tvProductsEmpty.visibility = if (items.isEmpty()) View.VISIBLE else View.GONE
    }

    private fun updateTags(selectedTag: TextView) {
        selectedCategory = selectedTag.text.toString()
        tags.forEach { tag ->
            val isSelected = tag == selectedTag
            tag.setBackgroundResource(if (isSelected) R.drawable.bg_tag_orange else R.drawable.bg_rounded_white)
            tag.setTypeface(null, if (isSelected) Typeface.BOLD else Typeface.NORMAL)
        }
        refreshProductList()
    }
}
