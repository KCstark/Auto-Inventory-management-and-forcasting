Inventory Forecasting – Documentation

Setting up:

SQL Query for creation of db:
create database data_collection

Add these 2 environment variable in IDE:
gemini-key = value
my-sql-pw = value

APIs: (Check postman collection shared for demo of all apis)

Authentication & Users

POST /api/v1/data/register

Request Body (UsersDTO):
{
  "name": "abc",
  "email": "abc@abc.com",
  "password": "password",
  "role": "USER"
}

Response: 201 Created with user details

POST /api/v1/data/login

Request Body:
{
  "email": "abc@abc.com",
  "password": "password"
}

Response: 200 OK
{
  "id": 1,
  "name": "abc",
  "email": "abc@abc.com",
  "role": "USER",
  "token": "JWT_TOKEN_HERE"
}

POST /api/v1/data/logout

Invalidates current JWT.
Response: 204 

GET /api/v1/data/users

Role: ADMIN only
Response: List of all users (UsersDTO).


Products
POST /api/v1/products

Role: ADMIN only

Request Body (ProductDTO):
{
  "name": "Book",
  "category": "BOOKS",
  "currentStock": 100,
  "reorderThreshold": 10,
  "leadTimeDays": 5,
  "price": 200.0
}

Response: ProductDTO with generated ID.

GET /api/v1/products/{id}

Role: Authenticated

Response (ProductDTO + Forecast):
{
  "id": 1,
  "name": "Book",
  "category": "BOOKS",
  "currentStock": 90,
  "reorderThreshold": 10,
  "leadTimeDays": 5,
  "price": 200.0,
  "latestForecast": {
    "id": 11,
    "forecastDate": "2025-08-23T00:00:00",
    "predictedDemand": 15.0,
    "algorithmUsed": "MOVING_AVERAGE"
  }
}


GET /api/v1/products

Role: Authenticated

Response: List of ProductDTOs.


Sales & Forecasts
POST /api/v1/sales

Role: USER/ADMIN

Triggers forecast recalculation.

Request Body (SalesDTO):

{
  "productId": 1,
  "quantity": 10
}

GET /api/v1/sales

Role: ADMIN

Returns all sales records.

GET /api/v1/forecasts

Role: Authenticated

Returns all forecasts.

GET /api/v1/forecasts?start=YYYY-MM-DD&end=YYYY-MM-DD

Role: Authenticated

Filters forecasts in given date range.

Response (ForecastDTO list):
[
  {
    "id": 11,
    "productId": 1,
    "forecastDate": "2025-08-23T00:00:00",
    "predictedDemand": 12.5,
    "algorithmUsed": "EXPONENTIAL_SMOOTHING"
  }
]

Purchase Orders
GET /api/v1/orders

Role: ADMIN

Returns all purchase orders.

POST /api/v1/orders/auto

Role: ADMIN

Generates orders for products below threshold.

PUT /api/v1/orders/{id}/receive

Role: ADMIN

Marks an order as RECEIVED and updates product stock.

Response (PurchaseOrderDTO):
{
  "id": 101,
  "productId": 1,
  "orderDate": "2025-08-20T12:00:00",
  "quantityOrdered": 50,
  "expectedArrivalDate": "2025-08-25T12:00:00",
  "status": "RECEIVED"
}

LLM Integration
POST /api/v1/llm/parse

Role: Authenticated

Accepts free-form text, returns structured JSON.

Request Example:
"Sold 45 units of blue office chairs, Furniture category, on July 15th to the West warehouse."
Response (StructuredSalesData):
{
  "productName": "blue office chairs",
  "category": "Furniture",
  "quantity": 45,
  "saleDate": "2025-07-15",
  "location": "West warehouse"
}

