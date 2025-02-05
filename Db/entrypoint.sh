#!/bin/bash

# Chạy SQL Server và tập lệnh khởi tạo cùng lúc
/opt/mssql/bin/sqlservr &

# Đợi cho SQL Server khởi động
echo "Waiting for SQL Server to start..."
sleep 30s  # Tăng thời gian chờ nếu cần thiết

# Chạy script SQL để tạo database và bảng
/opt/mssql-tools/bin/sqlcmd -S localhost -U sa -P $SA_PASSWORD -d master -i /usr/src/app/create-database.sql

# Bạn có thể thêm các script khác ở đây nếu cần
