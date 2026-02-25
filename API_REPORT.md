# PROJECT API DOCUMENTATION

## Overview
This document provides a comprehensive overview of the Tiffin Management API, including all controllers, endpoints, request/response formats, error formats, and frontend usage integration.

## 1. Authentication
**Currently NO authentication implemented** 
*(All APIs are open and accessible without tokens or headers)*

---

## 2. Global Error Response Format
When an API request fails (e.g., validation error, resource not found), the backend returns a standard JSON error response:

### Example: validation error (400 Bad Request)
```json
{
  "timestamp": "2026-02-25T14:30:00.000",
  "status": 400,
  "message": "Dish name cannot be empty",
  "path": "/menu_item"
}
```

### Example: not found error (404 Not Found)
```json
{
  "timestamp": "2026-02-25T14:35:00.000",
  "status": 404,
  "message": "Resource not found",
  "path": "/orders/999"
}
```

---

## 3. Pagination Format
For endpoints returning lists (e.g., GET Users, GET Orders), the response is formatted as a paginated object:

```json
{
  "content": [
    { ... }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "last": true,
  "totalElements": 25,
  "totalPages": 3,
  "first": true,
  "size": 10,
  "number": 0,
  "sort": { ... },
  "numberOfElements": 10,
  "empty": false
}
```

*(Note: In the frontend, the `api.js` script automatically extracts `.content` via `extractPaginatedData()`)*

---

## 4. API Endpoints by Group

### USER APIs

#### Create User
* **Method:** `POST`
* **Path:** `/users`
* **Description:** Creates a new customer/user.

**Request JSON:**
```json
{
  "name": "Uttam",
  "phone": "9999999999"
}
```

**Response JSON:**
```json
{
  "id": 1,
  "name": "Uttam",
  "phone": "9999999999"
}
```

#### Get All Users
* **Method:** `GET`
* **Path:** `/users?page={page}&size={size}`
* **Description:** Fetches all users with pagination.

**Response JSON:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Uttam",
      "phone": "9999999999"
    }
  ],
  "totalPages": 1,
  "totalElements": 1,
  "size": 10,
  "number": 0
}
```

---

### MENU APIs

#### Create Menu Item
* **Method:** `POST`
* **Path:** `/menu_item`
* **Description:** Adds a new dish to the menu.

**Request JSON:**
```json
{
  "dishName": "Paneer Butter Masala",
  "priceDefault": 120.50
}
```

**Response JSON:**
```json
{
  "id": 101,
  "dishName": "Paneer Butter Masala",
  "priceDefault": 120.50
}
```

#### Get All Menu Items
* **Method:** `GET`
* **Path:** `/menu_item`
* **Description:** Fetches all available dishes.

**Response JSON:**
```json
[
  {
    "id": 101,
    "dishName": "Paneer Butter Masala",
    "priceDefault": 120.50
  }
]
```

#### Update Menu Item
* **Method:** `PUT`
* **Path:** `/menu_item/{id}`
* **Description:** Updates details of an existing menu item.

**Request JSON:**
```json
{
  "dishName": "Paneer Butter Masala Special",
  "priceDefault": 150.00
}
```

**Response JSON:**
```json
{
  "id": 101,
  "dishName": "Paneer Butter Masala Special",
  "priceDefault": 150.00
}
```

#### Delete Menu Item
* **Method:** `DELETE`
* **Path:** `/menu_item/{id}`
* **Description:** Removes a dish from the menu.

**Response (No Body):**
```
204 No Content
```

---

### ORDER APIs

#### Create Order
* **Method:** `POST`
* **Path:** `/orders`
* **Description:** Places a new order for a user containing one or more menu items.

**Request JSON:**
```json
{
  "userId": 1,
  "items": [
    {
      "menuId": 101,
      "quantity": 2,
      "sellingPrice": 120.50
    }
  ]
}
```

**Response JSON:**
```json
{
  "id": 501,
  "userId": 1,
  "userName": "Uttam",
  "totalAmount": 241.00,
  "orderDate": "2026-02-25T10:15:30",
  "status": "CREATED",
  "items": [
    {
      "id": 1001,
      "menuId": 101,
      "dishName": "Paneer Butter Masala",
      "quantity": 2,
      "sellingPrice": 120.50,
      "itemSubtotal": 241.00
    }
  ]
}
```

#### Get All Orders
* **Method:** `GET`
* **Path:** `/orders?page={page}&size={size}`
* **Description:** Fetches all past and current orders with pagination.

**Response JSON:**
```json
{
  "content": [
    {
      "id": 501,
      "userId": 1,
      "userName": "Uttam",
      "totalAmount": 241.00,
      "orderDate": "2026-02-25T10:15:30",
      "status": "CREATED",
      "items": [
         {
           "id": 1001,
           "menuId": 101,
           "dishName": "Paneer Butter Masala",
           "quantity": 2,
           "sellingPrice": 120.50,
           "itemSubtotal": 241.00
         }
      ]
    }
  ],
  "totalPages": 5,
  "totalElements": 50,
  "size": 10,
  "number": 0
}
```

#### Get Order By ID
* **Method:** `GET`
* **Path:** `/orders/{id}`
* **Description:** Retrieves the full details of a specific order.

**Response JSON:** 
*(Same as Create Order response)*

#### Update Order Status
* **Method:** `PUT`
* **Path:** `/orders/{id}/status`
* **Description:** Updates the status of an order (e.g., to "COMPLETED").

**Request JSON:**
```json
{
  "status": "COMPLETED"
}
```

**Response JSON:**
```json
{
  "message": "Order status updated successfully"
}
```

---

### DASHBOARD & STATS (ANALYTICS) APIs

#### Get Dashboard Summary
* **Method:** `GET`
* **Path:** `/analytics/dashboard`
* **Description:** Gets aggregated stats for the dashboard overview.

**Response JSON:**
```json
{
  "totalUsers": 120,
  "totalOrders": 350,
  "todayOrders": 15,
  "totalRevenue": 45000.50,
  "todayRevenue": 2100.00,
  "mostOrderedDish": "Paneer Butter Masala"
}
```

#### Get Monthly Revenue
* **Method:** `GET`
* **Path:** `/analytics/revenue/monthly`
* **Description:** Returns revenue data grouped by month.

**Response JSON:**
```json
[
  {
    "month": "October",
    "revenue": 15000.00
  },
  {
    "month": "November",
    "revenue": 18500.00
  }
]
```

#### Get Daily Orders
* **Method:** `GET`
* **Path:** `/analytics/orders/daily?days={days}` *(default days: 30)*
* **Description:** Returns the count of orders for recent days.

**Response JSON:**
```json
[
  {
    "date": "2026-02-25",
    "orderCount": 15
  }
]
```

#### Get Top Customer
* **Method:** `GET`
* **Path:** `/analytics/users/top`
* **Description:** Returns the customer who has ordered the most or spent the most.

**Response JSON:** *(Returns 204 No Content if no data)*
```json
{
  "userName": "Uttam",
  "totalOrders": 45,
  "totalSpent": 12500.00
}
```

---

## 6. Frontend Usage Check
The frontend implementation (`Tiffin-management-frontend/js/`) successfully maps directly to these Spring Boot endpoints using the custom `apiFetch` wrapper.

**Key observations:**
- `ui.js`: Calls `/users` (GET/POST), `/menu_item` (GET/POST/PUT/DELETE). Pagination is used, passing `size=500` to fetch up to 500 records at once.
- `orders.js`: Calls `/orders` (GET/POST), `/orders/{id}` (GET), `/orders/{id}/status` (PUT). It sends the `status` both as a query parameter and JSON body when completing an order.
- `dashboard.js`: Correctly queries `/analytics/dashboard`, `/analytics/revenue/monthly`, `/analytics/users/top`, and `/orders?page=0&size=500` for recent order history.
- The global error handler in `api.js` accurately parses the Spring Boot backend standard error response format, extracting the `message` field reliably.
