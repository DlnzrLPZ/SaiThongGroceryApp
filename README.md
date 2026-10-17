1| <div align="center">
2| 
3| # 🛒 SaiThong Grocery App
4| 
5| **ร้านทรายทอง — a simple stock management app for a small grocery store**
6| 
7| ![Platform](https://img.shields.io/badge/platform-Android-3DDC84?logo=android&logoColor=white)
8| ![Language](https://img.shields.io/badge/language-Kotlin-7F52FF?logo=kotlin&logoColor=white)
9| ![Min SDK](https://img.shields.io/badge/minSdk-28-F37021)
10| ![Status](https://img.shields.io/badge/status-in%20development-yellow)
11| 
12| </div>
13| 
14| ---
15| 
16| ## About
17| 
18| SaiThong helps a grocery shop owner see what is in stock, add or edit products, and
19| keep track of what happened in the shop today. The UI is in Thai with an orange and
20| white minimalist design.
21| 
22| This is a **private app made for the shop owner only**. It has no multi-user login by design.
23| 
24| > 🚧 **Work in progress.** Data is currently stored in memory only, so it resets when
25| > the app is closed. A real backend is planned (see [Roadmap](#roadmap)).
26| 
27| ## Features
28| 
29| | Screen | What you can do |
30| |---|---|
31| | 🏠 **Home** | Dashboard with total products, low-stock count, and recent activity |
32| | 📦 **Products** | Browse all products, search by name, filter by category |
32| | ➕ **Add / ✏️ Edit** | Create or change a product (name, category, price, stock) with input validation |
32| | 🗑️ **Delete** | Confirm before deleting, with an **Undo** button right after |
32| | 📊 **Stock** | Overview of total, low-stock, and out-of-stock items |
32| | 🔄 **Update stock** | Change the stock of the product you picked with **+ / −** or by typing a number, and preview the new total before confirming |
32| | 🕘 **History** | Own tab in the bottom bar. Every add, edit, delete and stock change is listed by day with its remark, filterable by type. Kept for 3 months, then removed automatically |
32| | 📉 **Report** | Low-stock and out-of-stock lists (open from the Stock screen), with a shortcut to update each item |
33| 
34| **Stock levels**
35| - 🟢 In stock: more than 10
36| - 🟠 Low stock: 1 to 10 (`Product.LOW_STOCK_THRESHOLD`)
37| - 🔴 Out of stock: 0
38| 
39| ## Screenshots
40| 
41| <!-- Add screenshots here: create a /screenshots folder, drag the images in, then link them -->
42| <!-- | Home | Products | Stock | -->
43| <!-- |---|---|---| -->
44| <!-- | ![](screenshots/home.png) | ![](screenshots/products.png) | ![](screenshots/stock.png) | -->
45| 
46| _Screenshots coming soon._
47| 
48| ## Tech stack
49| 
50| - **Language:** Kotlin
51| - **UI:** XML layouts with View Binding, Material Components, RecyclerView
52| - **Navigation:** Bottom navigation bar (Home, Products, Stock, History) and Android back-button support
53| - **Build:** Gradle (Kotlin DSL), Android Gradle Plugin 9.3
54| - **SDK:** minSdk 28 · targetSdk 36 · compileSdk 37
55| - **Storage (now):** in-memory data store
56| 
57| ## Project structure
58| 
59| ```
60| app/src/main/
61| ├── java/com/pongsakorn/saithonggroceryapp/
62| │   ├── MainNavigation.kt     # Main screen + bottom navigation
63| │   ├── Home.kt               # Dashboard
64| │   ├── Products.kt           # Product list, search, category filter
65| │   ├── AddProduct.kt         # Add product form
66| │   ├── EditProduct.kt        # Edit product form
67| │   ├── DeleteFunction.kt     # Delete dialog + undo
68| │   ├── Stock.kt              # Stock overview
69| │   ├── UpdateStock.kt        # Update stock form (remark + confirm when set to 0)
70| │   ├── Report.kt             # Low-stock / out-of-stock report
71| │   ├── History.kt            # History tab: grouped by day, filter by type (3-month retention)
72| │   ├── HistoryAdapter.kt     # Day headers + log rows
73| │   ├── Product.kt            # Product model
74| │   ├── ProductData.kt        # In-memory product store
75| │   ├── ProductResources.kt   # Category icons and colors
76| │   ├── ActivityData.kt       # In-memory activity log
77| │   ├── ActivityLog.kt        # Activity log model
78| │   └── *Adapter.kt           # RecyclerView adapters
79| └── res/
80|     ├── layout/               # Screens and list items
81|     ├── drawable/             # Shapes and category images
82|     └── values/               # Strings (Thai), colors, themes
83| ```
84| 
85| ## Database Design
86| 
87| This is a practical ER model for the current app, with room for future sales reporting.
88| 
89| ```mermaid
90| erDiagram
91|     CATEGORY ||--o{ PRODUCT : contains
92|     PRODUCT ||--o{ ACTIVITY_LOG : records
93|     PRODUCT ||--o{ SALE_ITEM : appears_in
94|     SALE ||--o{ SALE_ITEM : includes
95| 
96|     CATEGORY {
97|         int id PK
98|         string name
99|     }
100| 
101|     PRODUCT {
102|         int id PK
103|         int category_id FK
104|         string name
105|         decimal price
106|         int stock_quantity
107|         int low_stock_threshold
108|         boolean is_active
109|         datetime created_at
110|         datetime updated_at
111|     }
112| 
113|     ACTIVITY_LOG {
114|         int id PK
115|         int product_id FK
116|         string action_type
117|         int previous_stock
118|         int new_stock
119|         int quantity_changed
120|         string remark
121|         datetime created_at
122|     }
123| 
124|     SALE {
125|         int id PK
126|         decimal subtotal
127|         decimal total_amount
128|         datetime sale_time
129|         string payment_method
130|     }
131| 
132|     SALE_ITEM {
133|         int id PK
134|         int sale_id FK
135|         int product_id FK
136|         int quantity
137|         decimal unit_price
138|         decimal line_total
139|     }
140| ```
141| 
142| ## Roadmap
143| 
144| - [x] Home dashboard and recent activity
145| - [x] Product list with search and category filter
146| - [x] Add, edit, delete (with undo)
147| - [x] Stock overview and stock update
148| - [ ] Save data permanently
149| - [ ] Backend: **Ktor + PostgreSQL** running in **Docker**
150| - [ ] Connect the app to the backend
151| - [x] Low-stock and out-of-stock reports
152| - [x] History page with 3-month retention
153| - [ ] Sales reports
154| 
155| ## Author
156| 
157| A college group project, made while learning Android development with Kotlin.
158| 
159| | Name | GitHub |
160| |---|---|
161| | Pongsakorn Watthana | [@DlnzrLPZ](https://github.com/DlnzrLPZ) |
162| | Patcharawadee Watthanaudomsuk | |
163| | Aryuphak Lengthaisong | |
164| | Suthakorn Phanphrm | |
165| | Jirayut Raengkhetkarn | |
166| 