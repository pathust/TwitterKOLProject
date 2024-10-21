# TwitterKOLProject

## Tổng Quan
- Tự động tìm danh sách KOL trong blockchain trên Twitter bằng cách sử dụng Selenium. 
- Thu thập các tweet và followers của KOL, xây dựng đồ thị mạng xã hội và tính điểm Pagerank để xếp hạng các KOL.

## Bắt Đầu

### Yêu Cầu
Đảm bảo đã cài đặt:
- Selenium
- JavaFx
- Chromedriver

### Sao Chép Kho Lưu Trữ
1. Sao chép kho lưu trữ bằng cách sử dụng lệnh sau:
   ```bash
   git clone https://github.com/pathust/TwitterKOLProject
   cd TwitterKOLProject
   ```

### Thực Hiện Thay Đổi Trên Nhánh Mới
1. Chuyển sang main branch và pull các thay đổi mới nhất:
   ```bash
   git checkout main
   git pull origin main
   ```

2. Tạo và chuyển sang nhánh mới:
   ```bash
   git checkout -b <tên-nhánh>
   ```

3. Thực hiện các thay đổi của bạn trong các Project.
4. Đưa các thay đổi vào khu vực tạm thời:
   ```bash
   git add .
   ```

5. Commit các thay đổi của bạn với một thông điệp ý nghĩa:
   ```bash
   git commit -m "Mô tả các thay đổi"
   ```

### Đẩy Nhánh Lên
Đẩy nhánh lên remote repo:
```bash
git push origin <tên-nhánh>
```

### Mở Pull Request
1. Truy cập kho lưu trữ trên GitHub.
2. Nhấp vào tab "Pull requests".
3. Nhấp vào "New pull request."
4. Chọn nhánh của bạn làm nhánh so sánh và nhánh chính làm nhánh cơ sở.
5. Điền tiêu đề và mô tả, sau đó nhấp vào "Create pull request."

### Đánh Giá Mã
Sau khi bạn đã mở pull request, Tài sẽ review code. Sẵn sàng để giải quyết bất kỳ nhận xét hoặc đề xuất nào được đưa ra.

### Gộp Thay Đổi
Khi đã hoàn tất việc review, Tài sẽ merge các thay đổi vào nhánh chính. Nếu cần, sẽ xóa nhánh sau khi gộp.
