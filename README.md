# Java Backend API | Sistem Pemesanan Villa

---

# Identitas Kelompok

* Nama  : I Gusti Bagus Narendratanaya Wiweka  
  NIM   : 2405551007  
  Mata Kuliah : PBO (E)

* Nama  : I Made Ivan Ari Mahayana <br>
  NIM   : 2405551015  
  Mata Kuliah : PBO (E)

* Nama  : Anak Agung Ngurah Bramanda Maha Saputra  
  NIM   : 2405551018  
  Mata Kuliah : PBO (E)

* Nama  : I Putu Weda Sidhi Putra  
  NIM   : 2405551164  
  Mata Kuliah : PBO (E)

---

## 📦 Introducing

Halo! Ini adalah proyek backend Application Programming Interface (API) sederhana yang dibangun menggunakan **Java** tanpa framework eksternal dan menggunakan arsitektur modular berbasis **Handler-DAO-Model**, proyek ini kami buat untuk memenuhi Tugas 2 Mata Kuliah Pemrograman Berorientasi Objek (PBO). API ini berfungsi untuk mengelola data berbagai entitas seperti **Villa**, **Customer**, **Booking**, **Voucher**, dan lainnya. API ini mendukung metode Hypertext Transfer Protocol (HTTP) **GET**, **POST**, **PUT**, dan **DELETE**, serta mengembalikan response dalam format **JSON**.

Penyimpanan data dilakukan menggunakan **SQLite**, dan pengujian dilakukan dengan bantuan aplikasi **Postman** pada endpoint `http://localhost:8080`.

---

## 🧱 Struktur

Berikut adalah penjelasan dari struktur program berdasarkan folder:

### 📁 `src`

#### ➤ `DAO`
Kelas-kelas ini menangani komunikasi langsung dengan **database SQLite**, seperti menambahkan, membaca, memperbarui, dan menghapus data.
- `BookingDAO`
- `CustomerDAO`
- `ReviewDAO`
- `RoomTypesDAO`
- `VillaDAO`
- `VoucherDAO`
- `Database` – Mengatur koneksi ke SQLite

#### ➤ `Exception`
Berisi kelas-kelas exception kustom yang digunakan untuk menangani error secara eksplisit dalam API dan menampilkan response sesuai standar HTTP.
- `BadRequestException`
- `MethodNotAllowedException`
- `NotFoundException`

#### ➤ `Handler`
Kelas-kelas ini berfungsi sebagai penghubung antara HTTP request dari client dan operasi data melalui DAO. Handler membaca request, memprosesnya, dan mengirimkan response yang sesuai.
- `CustomerHandler`
- `VillaHandler`
- `VoucherHandler`

#### ➤ `Model`
Berisi representasi dari setiap entitas di database dalam bentuk class Java, digunakan untuk pertukaran dan pemrosesan data di dalam aplikasi.
- `Booking`
- `Customer`
- `Review`
- `RoomType`
- `Villa`
- `Voucher`

#### ➤ `Http`
Berisi komponen utama untuk menjalankan server HTTP dan menangani komunikasi client-server:
- `Request` – Untuk parsing HTTP request dari client.
- `Response` – Untuk membentuk HTTP response ke client.
- `Server` – Untuk menjalankan web server lokal pada port `8080`.
- `Main` – Class utama untuk mengeksekusi aplikasi.

---

## 🗃️ Resource & Library

### 📁 `database`
- `vbook.db` – File database SQLite yang menyimpan seluruh data.
- `villa_booking.sql` – Skrip Structured Query Language (SQL) untuk inisialisasi struktur tabel.

### 📁 `lib`
Berisi seluruh dependensi eksternal yang digunakan, diletakkan secara manual:
- `jackson-annotations-2.13.3.jar`  
- `jackson-core-2.13.3.jar`  
- `jackson-databind-2.13.3.jar`  
- `sqlite-jdbc-3.36.0.3.jar`  

### 📄 `villa.iml`
File konfigurasi modul IntelliJ IDEA yang mendefinisikan struktur modul dan dependensi dalam proyek ini.

> Semua `.jar` digunakan untuk memproses JSON (`Jackson`) dan koneksi ke database SQLite (`sqlite-jdbc`).

---

## ▶️ Cara Menjalankan

1. Pastikan Java Development Kit (JDK) sudah terinstal.
2. Jalankan file `Main.java` sebagai program utama.
3. Akses endpoint API di browser atau Postman melalui:
   ```
   http://localhost:8080
   ```

---

## 🧪 Test in Postman

Setelah server berjalan, gunakan Postman untuk menguji API. Berikut adalah daftar endpoint yang tersedia untuk masing-masing entitas:

### 🏡 VILLA

| Endpoint                                               | Method  | Fungsi                                                             |
|--------------------------------------------------------|---------|--------------------------------------------------------------------|
| `/ping`                                                | GET     | Mengecek apakah server aktif                                       |
| `/villas`                                              | GET     | Menampilkan semua data villa                                       |
| `/villas/{id}`                                         | GET     | Menampilkan detail sebuah villa berdasarkan ID                     |
| `/villas/{id}/rooms`                                   | GET     | Menampilkan daftar tipe kamar pada villa tertentu                  |
| `/villas/{id}/bookings`                                | GET     | Menampilkan semua pemesanan pada villa tertentu                    |
| `/villas/{id}/reviews`                                 | GET     | Menampilkan semua ulasan untuk villa tertentu                      |
| `/villas?ci_date={checkin_date}&co_date={checkout_date}` | GET   | Menampilkan villa berdasarkan ketersediaan tanggal check-in/out    |
| `/villas`                                              | POST    | Menambahkan data villa baru                                        |
| `/villas/{id}/rooms`                                   | POST    | Menambahkan tipe kamar ke villa                                    |
| `/villas/{id}`                                         | PUT     | Memperbarui data villa                                             |
| `/villas/{id}/rooms/{id}`                              | PUT     | Memperbarui tipe kamar tertentu pada villa                         |
| `/villas/{id}/rooms/{id}`                              | DELETE  | Menghapus tipe kamar tertentu dari villa                           |
| `/villas/{id}`                                         | DELETE  | Menghapus data villa                                               |

---

### 👤 CUSTOMER

| Endpoint                                       | Method | Fungsi                                                         |
|------------------------------------------------|--------|----------------------------------------------------------------|
| `/customers`                                   | GET    | Menampilkan semua data customer                                |
| `/customers/{id}`                              | GET    | Menampilkan detail customer berdasarkan ID                     |
| `/customers/{id}/bookings`                     | GET    | Menampilkan semua pemesanan oleh customer tertentu             |
| `/customers/{id}/reviews`                      | GET    | Menampilkan semua review yang dibuat oleh customer tertentu    |
| `/customers`                                   | POST   | Menambahkan data customer baru                                 |
| `/customers/{id}/bookings`                     | POST   | Membuat booking baru oleh customer tertentu                    |
| `/customers/{id}/bookings/{id}/reviews`        | POST   | Menambahkan review terhadap booking tertentu oleh customer     |
| `/customers/{id}`                              | PUT    | Memperbarui data customer                                      |

---

### 🎟️ VOUCHER

| Endpoint                  | Method | Fungsi                                   |
|---------------------------|--------|------------------------------------------|
| `/vouchers`              | GET    | Menampilkan semua data voucher           |
| `/vouchers/{id}`         | GET    | Menampilkan detail voucher berdasarkan ID|
| `/vouchers`              | POST   | Menambahkan data voucher baru            |
| `/vouchers/{id}`         | PUT    | Memperbarui data voucher tertentu        |
| `/vouchers/{id}`         | DELETE | Menghapus data voucher tertentu          |

---

## 📷 Documentation in Postman

Aplikasi ini sudah bisa dijalankan pada `localhost:8080` , dan pengujian dilakukan menggunakan Postman untuk mempermudah proses pengujian endpoint. Berikut dokumentasi pada halaman postman: 

### 🔍 GET in Villas
Menampilkan semua daftar villa
`http://localhost:8080/villas`
![Screenshot 2025-06-30 122226](https://github.com/user-attachments/assets/ad3e323b-0dca-44a2-b178-ebe07697100e)

Mencoba test ping pada villa
` http://localhost:8080/ping`
![image](https://github.com/user-attachments/assets/4d8ea741-7d7b-43c1-97a5-7504eeda63c0)

Menampilkan villa sesuai dengan id
`http://localhost:8080/villas/1`
![image](https://github.com/user-attachments/assets/789674e8-5f6c-4c8a-8f47-7bf1d1a93284)

Menampilkan isi dari ruangan villa sesuai dengan ID villa
`http://localhost:8080/villas/1/rooms`
![image](https://github.com/user-attachments/assets/37e4d3c9-a590-425c-93b9-61b71eaa7d08)

Menampilkan data booking pada Villa yang memiliki ID 1 
`http://localhost:8080/villas/1/bookings`
![image](https://github.com/user-attachments/assets/f27f817e-9855-4d4e-8890-0e4e4a595cfd)

Menampilkan isi riview dari Villa berdasarkan id Villa
`http://localhost:8080/villas/1/reviews`
![image](https://github.com/user-attachments/assets/8fef7505-1ed0-4fe3-bb43-3fabb3fc1522)

Menampilkan info taggal checkin dan chekout dari data villa booking
` http://localhost:8080/villas?ci_date=2025-06-20&co_date=2025-06-25`
![image](https://github.com/user-attachments/assets/0af394a3-11f7-4495-9419-cd995fc08e0b)

### ➕ POST in Villas
Menambahkan Villa baru
`http://localhost:8080/villas`
![image](https://github.com/user-attachments/assets/6f175cbc-a783-4f84-a28c-baaaa208f867)

Menambahkan Ruangan Villa baru
`http://localhost:8080/villas/1/rooms`
![image](https://github.com/user-attachments/assets/876661a6-2b0a-42a4-b9c9-c5eea459e34b)

### ✏️ PUT in Villas
Mengubah isi data villa 
`http://localhost:8080/villas/1`
![image](https://github.com/user-attachments/assets/d720a5fb-ec77-4b22-b8cb-351bc79a11e4)

Mengubah isi data ruangan Villa
`http://localhost:8080/villas/1/rooms/1`
![image](https://github.com/user-attachments/assets/dfb04181-5c8e-4064-abad-757e7a6161d0)

### 🗑️ DELETE in Villa
Menghapus ruangan Villa
`http://localhost:8080/villas/1/rooms/1`
![image](https://github.com/user-attachments/assets/0ff3e522-b2ad-494b-9bd6-c965f406dc0e)

Menghapus data villa
`http://localhost:8080/villas/1`
![Screenshot 2025-06-30 165856](https://github.com/user-attachments/assets/c8888e85-7f1d-4e44-a203-b66a6780af5a)

---

### 🔍 GET in Customers

Menampilkan daftar semua customer
`http://localhost:8080/customers`
![image](https://github.com/user-attachments/assets/94f9fadf-ec5f-476f-8aa3-41ac9bab2b58)

Menampilkan informasi detail seorang customer
`http://localhost:8080/customers/{id}`
![image](https://github.com/user-attachments/assets/09c86260-e39b-4398-b024-bb45dd472a32)

Menampilkan daftar booking yang telah dilakukan oleh seorang customer
`http://localhost:8080/customers/{id}/bookings`
![image](https://github.com/user-attachments/assets/00313d74-714d-4497-9adb-0c2f85cf98fd)

Menampilkan daftar ulasan yang telah diberikan oleh customer
`http://localhost:8080/customers/{id}/reviews`
![image](https://github.com/user-attachments/assets/79ff1feb-e3e6-41dd-bfbe-bef4c06fb347)

### ➕ POST in Customers

Menambahkan customer baru (registrasi customer)
`http://localhost:8080/customers`
![image](https://github.com/user-attachments/assets/2190bf47-f0f4-46d1-be68-c9735aaf1311)

Customer melakukan pemesanan vila
`http://localhost:8080/customers/{id}/bookings`
![image](https://github.com/user-attachments/assets/bde24302-712f-4b18-a109-8b669db25691)

Customer memberikan ulasan pada vila (berdasarkan informasi booking)
`http://localhost:8080/customers/{id}/bookings/{id}/reviews`
![image](https://github.com/user-attachments/assets/0381bf66-227e-42d7-ba88-9e8549a57eea)

### ✏️ PUT in Customers

Mengubah data seorang customer
`http://localhost:8080/customers/{id}`
![image](https://github.com/user-attachments/assets/9064b560-1ac6-407e-bc85-ecfdde7ce4d4)

---

### 🔍 GET in Voucher

Menampilkan daftar semua voucher
`http://localhost:8080/vouchers`
![image](https://github.com/user-attachments/assets/fe9d3308-a583-4a0d-9c96-66aefbf210e8)

Menampilkan informasi detail suatu voucher
`http://localhost:8080/vouchers/{id}`
![image](https://github.com/user-attachments/assets/a4f7f7e1-6f2b-45a5-a7a4-d837ed9779f1)

### ➕ POST in Voucher

Membuat voucher baru
`http://localhost:8080/vouchers`
![image](https://github.com/user-attachments/assets/0daa0dc5-70e9-45f2-8643-9799878e88c8)

### ✏️ PUT in Voucher

Mengubah data suatu voucher
`http://localhost:8080/vouchers/{id}`
![image](https://github.com/user-attachments/assets/f8450af1-bd43-4f32-a2bc-fba082e578c3)

### 🗑️ DELETE in Voucher

Menghapus data suatu voucher
`http://localhost:8080/vouchers/{id}`
![image](https://github.com/user-attachments/assets/9ddfb831-c5b5-4bf8-913a-2b327ed5001b)

---

### ❌ Error Response 404
Ketika melakukan perubahan data pada entitas (seperti melakukan DELETE), tetapi entitasnya tidak ada
`http://localhost:8080/villas/{id}`
![Screenshot 2025-06-30 171649](https://github.com/user-attachments/assets/8500e76e-065a-4054-83f3-e5efb2be4e0d)

---

> 📌 Jangan lupa untuk menyertakan header berikut saat menggunakan metode `POST` dan `PUT`:
```
Content-Type: application/json
```

---

## 📚 Teknologi

- Bahasa Pemrograman: **Java**
- Database: **SQLite** (`vbook.db`)
- Library JSON: **Jackson**
- JDBC Driver: **SQLite JDBC**
- Tools uji: **Postman**

---

# Terima kasih telah membaca dokumentasi kami! 🎉
