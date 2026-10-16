<div align="center">

# 🛒 SaiThong Grocery App

**ร้านทรายทอง — a simple stock management app for a small grocery store**

![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
![Language](https://img.shields.io/badge/language-Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Min SDK](https://img.shields.io/badge/minSdk-28-F37021)
![Status](https://img.shields.io/badge/status-in%20development-yellow)

</div>

---

## About

SaiThong helps a grocery shop owner see what is in stock, add or edit products, and
keep track of what happened in the shop today. The UI is in Thai with an orange and
white minimalist design.

This is a **private app made for the shop owner only**. It has no multi-user login by design.

> 🚧 **Work in progress.** Data is currently stored in memory only, so it resets when
> the app is closed. A real backend is planned (see [Roadmap](#roadmap)).

## Features

| Screen | What you can do |
|---|---|
| 🏠 **Home** | Dashboard with total products, low-stock count, and recent activity |
| 📦 **Products** | Browse all products, search by name, filter by category |
| ➕ **Add / ✏️ Edit** | Create or change a product (name, category, price, stock) with input validation |
| 🗑️ **Delete** | Confirm before deleting, with an **Undo** button right after |
| 📊 **Stock** | Overview of total, low-stock, and out-of-stock items |
| 🔄 **Update stock** | Change the stock of the product you picked with **+ / −** or by typing a number, and preview the new total before confirming |
| 📝 **History** | Every add, edit, delete and stock change is recorded with a time and its remark. Kept for 3 months, then removed automatically |
| 📉 **Report** | Low-stock and out-of-stock lists (open from the Stock screen), with a shortcut to update each item |

**Stock levels**
- 🟢 In stock: more than 10
- 🟠 Low stock: 1 to 10
- 🔴 Out of stock: 0

## Screenshots

<!-- Add screenshots here: create a /screenshots folder, drag the images in, then link them -->
<!-- | Home | Products | Stock | -->
<!-- |---|---|---| -->
<!-- | ![](screenshots/home.png) | ![](screenshots/products.png) | ![](screenshots/stock.png) | -->

_Screenshots coming soon._

## Tech stack

- **Language:** Kotlin
- **UI:** XML layouts with View Binding, Material Components, RecyclerView
- **Navigation:** Bottom navigation bar (Home, Products, Stock)
- **Build:** Gradle (Kotlin DSL), Android Gradle Plugin 9.3
- **SDK:** minSdk 28 · targetSdk 36 · compileSdk 37
- **Storage (now):** in-memory data store

## Project structure

```
app/src/main/
├── java/com/pongsakorn/saithonggroceryapp/
│   ├── MainNavigation.kt     # Main screen + bottom navigation
│   ├── Home.kt               # Dashboard
│   ├── Products.kt           # Product list, search, category filter
│   ├── AddProduct.kt         # Add product form
│   ├── EditProduct.kt        # Edit product form
│   ├── DeleteFunction.kt     # Delete dialog + undo
│   ├── Stock.kt              # Stock overview
│   ├── UpdateStock.kt        # Update stock form (remark + confirm when set to 0)
│   ├── Report.kt             # Low-stock / out-of-stock report
│   ├── History.kt            # Full activity history (3-month retention)
│   ├── Product.kt            # Product model
│   ├── ProductData.kt        # In-memory product store
│   ├── ProductResources.kt   # Category icons and colors
│   ├── ActivityData.kt       # In-memory activity log
│   ├── ActivityLog.kt        # Activity log model
│   └── *Adapter.kt           # RecyclerView adapters
└── res/
    ├── layout/               # Screens and list items
    ├── drawable/             # Shapes and category images
    └── values/               # Strings (Thai), colors, themes
```

## Roadmap

- [x] Home dashboard and recent activity
- [x] Product list with search and category filter
- [x] Add, edit, delete (with undo)
- [x] Stock overview and stock update
- [ ] Save data permanently
- [ ] Backend: **Ktor + PostgreSQL** running in **Docker**
- [ ] Connect the app to the backend
- [x] Low-stock and out-of-stock reports
- [x] History page with 3-month retention
- [ ] Sales reports

## Author

A college group project, made while learning Android development with Kotlin.

| Name | GitHub |
|---|---|
| Pongsakorn Watthana | [@DlnzrLPZ](https://github.com/DlnzrLPZ) |
| Patcharawadee Watthanaudomsuk | |
| Aryuphak Lengthaisong | |
| Suthakorn Phanphrm | |
| Jirayut Raengkhetkarn | |
