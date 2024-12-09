#Link chat: https://chatgpt.com/share/675668ed-1838-8013-8e00-7acf15ff2233

Sample format request body to place order
{
  "customer": {
    "customerId": null,
    "customerName": "Hanh My",
    "customerEmail": "ductai0110203@gmail.com",
    "customerPhone": "123-456-7890",
    "shippingAddress": "123 Main St, Anytown, USA"
  },
  "order": {
    "shippingMethod": "Standard",
    "paymentMethod": "Credit Card",
    "notes": "Please deliver before 5 PM"
  },
  "orderItems": [
    {
      "productId": 1,
      "productName": "Product A",
      "price": 50.0,
      "quantity": 2
    },
    {
      "productId": 2,
      "productName": "Product B",
      "price": 100.0,
      "quantity": 1
    }
  ]
}
