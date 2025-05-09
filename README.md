# 🎵 MusicLD - Nền tảng sáng tác và chia sẻ âm nhạc

## 🧑‍🎤 Giới thiệu

**MusicLD** là một nền tảng dành cho những người yêu âm nhạc, cho phép **sáng tác, chia sẻ, khám phá và tương tác** với các bài hát từ cộng đồng. Nền tảng này cung cấp giao diện thân thiện cùng các tính năng mạnh mẽ để giúp người dùng phát huy khả năng âm nhạc của mình và kết nối với những người có cùng đam mê.

---

## 🚀 Tính năng

### 👤 Dành cho người dùng

#### 🎵 Quản lý nhạc
- Tải lên và chia sẻ nhạc cá nhân.
- Chỉnh sửa thông tin bài hát (tiêu đề, thể loại, lời,...).
- Xoá bài hát khỏi thư viện cá nhân.

#### 🎧 Trải nghiệm nghe nhạc
- Trình phát nhạc trực tuyến chất lượng cao.
- Tạo danh sách phát tuỳ chỉnh.
- Khám phá nhạc mới từ cộng đồng.

#### 👍 Tương tác cộng đồng
- Thả tim bài hát yêu thích.
- Bình luận, phản hồi trên bài hát.
- Chia sẻ bài hát lên mạng xã hội.

#### 👥 Trang cá nhân
- Hồ sơ hiển thị thông tin và hoạt động.
- Quản lý các bài hát đã đăng.
- Lịch sử nghe nhạc và tương tác.

#### 💬 Giao tiếp
- Chat cộng đồng theo thời gian thực (WebSocket).
- Trợ lý ảo AI gợi ý sáng tác, hỗ trợ nhạc lý,...

### 🛡️ Dành cho quản trị viên
- Quản lý và kiểm duyệt bài hát.
- Giám sát và xoá các bình luận vi phạm.
- Quản lý tài khoản người dùng.

---

## 💰 Chính sách đăng bài

- **Mỗi người dùng được đăng miễn phí 2 bài hát đầu tiên**.
- Từ bài thứ 3, người dùng cần thanh toán qua **VNPAY** để tiếp tục đăng.

---

## 🔐 Xác thực & Đăng nhập

Hỗ trợ xác thực người dùng qua:
- Email & mật khẩu
- Google OAuth2
- Facebook OAuth2

---

## 🧩 Công nghệ sử dụng

### ⚙️ Backend
- `Java Spring Boot` - RESTful API
- `Spring Security + JWT` - Xác thực & phân quyền
- `Redis` - Cache và quản lý phiên
- `WebSocket` - Chat thời gian thực
- `MySQL` hoặc `SQL Server` - Cơ sở dữ liệu quan hệ
- `Cloudinary` - Lưu trữ file nhạc, ảnh
- `VNPAY` - Tích hợp thanh toán

---

## 📂 Cài đặt & Chạy dự án (dev)

```bash
# Clone repo Backend
git clone https://github.com/leduy2004-coder/MusicLD-Backend.git
# Clone repo Frontend
git clone https://github.com/leduy2004-coder/MusicLD-Frontend.git

# Mở bằng IDE yêu thích (IntelliJ, VS Code...)

# Cấu hình các file:
# - application.yml (hoặc application.properties)
# - local.env (nếu dùng dotenv)
# - Redis, MySQL, Cloudinary, VNPAY credentials

# Chạy ứng dụng
./mvnw spring-boot:run
