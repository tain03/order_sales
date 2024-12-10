---

# Order Sales System

Dự án **Order Sales System** là một ứng dụng quản lý đơn hàng được xây dựng với Spring Boot. Hệ thống hỗ trợ việc tạo mới, quản lý và theo dõi trạng thái của các đơn hàng từ khi đặt hàng đến khi hoàn tất. Dự án này giúp tự động hóa quy trình xử lý đơn hàng, giúp người dùng dễ dàng theo dõi trạng thái và chi tiết đơn hàng.

## Tính Năng Chính

1. **Quản lý đơn hàng**:
   - Người dùng có thể tạo, cập nhật và truy vấn thông tin đơn hàng.
   - Hệ thống cho phép theo dõi trạng thái đơn hàng (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED).

2. **Quản lý khách hàng**:
   - Khách hàng có thể được tạo mới hoặc tìm kiếm qua email.
   - Hệ thống hỗ trợ tìm kiếm khách hàng theo thông tin như email, tên, số điện thoại, và địa chỉ giao hàng.

3. **Quản lý sản phẩm**:
   - Hệ thống cho phép thêm các sản phẩm vào đơn hàng và tính toán tổng số tiền đơn hàng.

4. **Gửi email xác nhận đơn hàng**:
   - Khi đơn hàng được tạo, hệ thống sẽ gửi email xác nhận đến khách hàng với thông tin chi tiết về đơn hàng.

5. **Quản lý trạng thái đơn hàng**:
   - Người dùng có thể thay đổi trạng thái của đơn hàng từ PENDING sang các trạng thái khác như PROCESSING, SHIPPED, DELIVERED, hoặc CANCELLED.
   - Hệ thống sẽ ghi lại lịch sử thay đổi trạng thái của đơn hàng trong bảng `OrderTransaction`.

## Các Thành Phần

- **Spring Boot**: Dự án sử dụng Spring Boot để xây dựng backend RESTful API.
- **JPA/Hibernate**: Để quản lý cơ sở dữ liệu và thực hiện các thao tác CRUD với các bảng `Customer`, `Order`, `OrderItem`, `OrderTransaction`, và `Product`.
- **Swagger**: Dùng để tự động tạo và hiển thị tài liệu API, giúp dễ dàng kiểm thử và tích hợp API.
- **Email Service**: Sử dụng để gửi email xác nhận đơn hàng cho khách hàng khi đơn hàng được tạo thành công.
- **Database**: Sử dụng cơ sở dữ liệu để lưu trữ thông tin về khách hàng, đơn hàng, sản phẩm và lịch sử giao dịch.

## Cấu Trúc Dự Án

```
order-sales-system/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com/
│   │   │   │   ├── example/
│   │   │   │   │   ├── order_sales/
│   │   │   │   │   │   ├── controller/           # Các controller cho các API
│   │   │   │   │   │   ├── dto/                   # Các lớp DTO dùng để truyền nhận dữ liệu
│   │   │   │   │   │   ├── entity/                # Các entity lớp mô tả bảng trong DB
│   │   │   │   │   │   ├── repository/            # Các repository truy vấn DB
│   │   │   │   │   │   └── service/               # Các service chứa logic của ứng dụng
├── resources/
│   └── application.properties                       # Cấu hình cơ sở dữ liệu và các thiết lập khác
└── pom.xml                                          # Các phụ thuộc Maven
```

## Cài Đặt và Chạy Ứng Dụng

### Yêu Cầu

- JDK 17 hoặc cao hơn
- Maven 3.6 hoặc cao hơn
- Cơ sở dữ liệu MySQL hoặc PostgreSQL

### Cài Đặt

1. **Clone Repository**

```bash
git clone https://github.com/your-repository/order-sales-system.git
cd order-sales-system
```

2. **Cài Đặt Các Phụ Thuộc**

Chạy lệnh sau để cài đặt các phụ thuộc Maven:

```bash
mvn clean install
```

3. **Cấu Hình Cơ Sở Dữ Liệu**

Cập nhật thông tin kết nối cơ sở dữ liệu trong file `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/order_sales
spring.datasource.username=root
spring.datasource.password=password
spring.jpa.hibernate.ddl-auto=update
```

4. **Chạy Ứng Dụng**

Sau khi cài đặt và cấu hình, bạn có thể chạy ứng dụng bằng lệnh:

```bash
mvn spring-boot:run
```

Ứng dụng sẽ chạy trên `http://localhost:8080`.

### API Chính

#### 1. **Tạo Đơn Hàng**

- **Endpoint**: `/api/orders`
- **Method**: `POST`
- **Mô tả**: Tạo một đơn hàng mới, nếu khách hàng chưa có thì sẽ tạo mới khách hàng.
- **Request Body**: 
  ```json
  {
    "customer": {
      "customerId": 1,
      "customerName": "Nguyễn Văn A",
      "customerEmail": "nguyenvana@gmail.com",
      "customerPhone": "0901234567",
      "shippingAddress": "123 Đường ABC, TP.HCM"
    },
    "order": {
      "shippingMethod": "Express",
      "paymentMethod": "Credit Card",
      "notes": "Giao hàng nhanh"
    },
    "orderItems": [
      {
        "productId": 101,
        "quantity": 2
      },
      {
        "productId": 102,
        "quantity": 1
      }
    ]
  }
  ```

#### 2. **Cập Nhật Trạng Thái Đơn Hàng**

- **Endpoint**: `/api/orders/{orderId}/status`
- **Method**: `PUT`
- **Mô tả**: Cập nhật trạng thái của đơn hàng (PENDING, PROCESSING, SHIPPED, DELIVERED, CANCELLED).
- **Request Body**:
  ```json
  {
    "status": "SHIPPED"
  }
  ```

#### 3. **Lấy Thông Tin Đơn Hàng**

- **Endpoint**: `/api/orders`
- **Method**: `GET`
- **Mô tả**: Lấy danh sách tất cả các đơn hàng.

## License

Dự án này được phát hành theo giấy phép MIT. Vui lòng tham khảo file LICENSE để biết thêm chi tiết.

---
