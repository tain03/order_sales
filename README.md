#Link chat: [https://chatgpt.com/share/675668ed-1838-8013-8e00-7acf15ff2233](https://chatgpt.com/share/675668ed-1838-8013-8e00-7acf15ff2233)

Sample format request body to place order
{
  "customer": {
    "customerName": "Nguyen Van A",
    "customerEmail": "nguyen@email.com",
    "customerPhone": "0123456789",
    "shippingAddress": "123 ABC Street"
  },
  "order": {
    "shippingMethod": "Express",
    "paymentMethod": "Credit Card",
    "notes": "Please deliver urgently"
  },
  "orderItems": [
    {
      "productId": 1,
      "quantity": 2
    },
    {
      "productId": 2,
      "quantity": 1
    }
  ]
}

