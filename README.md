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

Aplikasi ini sudah bisa dijalankan pada `localhost:8080`, dan pengujian dilakukan menggunakan Postman untuk mempermudah proses pengujian endpoint. Berikut dokumentasi pada halaman Postman: 

### 🌐 GET in Default Handler
Mencoba test GET pada default handler  <br>
`http://localhost:8080/`
![Screenshot 2025-06-30 173625](https://github.com/user-attachments/assets/35c4c045-fd8c-47bf-a59c-f2e099286bfc)

### 🌐 Test Ping pada Postman
Mencoba test ping menggunakan endpoint `/ping` <br>
`http://localhost:8080/ping`
![image](https://github.com/user-attachments/assets/4d8ea741-7d7b-43c1-97a5-7504eeda63c0)

---

### 🔍 GET in Villas
Menampilkan semua daftar villa <br>
`http://localhost:8080/villas`
![image](https://github.com/user-attachments/assets/509a46f5-1f26-4091-99b7-06f1245b3e95)

Menampilkan villa sesuai dengan ID <br>
`http://localhost:8080/villas/{id}`
![image](https://github.com/user-attachments/assets/06217b10-696d-4eba-9ef4-42c93242e9ec)

Menampilkan isi dari ruangan villa sesuai dengan ID villa <br>
`http://localhost:8080/villas/{id}/rooms`
![image](https://github.com/user-attachments/assets/1cea1297-0586-4e96-8bda-2da84ba408f5)

Menampilkan data booking pada villa yang memiliki ID 1 <br>
`http://localhost:8080/villas/{id}/bookings`
![image](https://github.com/user-attachments/assets/4cf92ff5-057c-4272-8cb0-b6ae7d5fdf62)

Menampilkan isi review dari villa berdasarkan ID villa <br>
`http://localhost:8080/villas/{id}/reviews`
![image](https://github.com/user-attachments/assets/7941add1-938b-4e02-8e16-987ac8141596)

Menampilkan info tanggal check-in dan check-out dari data villa booking <br>
` http://localhost:8080/villas?ci_date=2025-06-20&co_date=2025-06-25`
![image](https://github.com/user-attachments/assets/60d9b549-dd08-4023-9c8c-0f8bc7aa21d0)

### ➕ POST in Villas
Menambahkan villa baru <br>
`http://localhost:8080/villas`
![image](https://github.com/user-attachments/assets/86f9267c-7179-47af-b1b8-d9ba929b7cdd)

Menambahkan ruangan villa baru <br>
`http://localhost:8080/villas/{id}/rooms`
![image](https://github.com/user-attachments/assets/b1da5181-a3f0-48a2-aceb-701e9144219d)

### ✏️ PUT in Villas
Mengubah isi data villa <br>
`http://localhost:8080/villas/{id}`
![image](https://github.com/user-attachments/assets/899afa3e-d935-4cfc-9f8b-c9b7bea3f348)

Mengubah isi data ruangan villa <br>
`http://localhost:8080/villas/{id}/rooms/{id}`
![image](https://github.com/user-attachments/assets/811d0451-9220-46ea-b948-69672794d9b2)

### 🗑️ DELETE in Villas
Menghapus ruangan villa <br>
`http://localhost:8080/villas/{id}/rooms/{id}`
![image](https://github.com/user-attachments/assets/f6825632-90c2-4123-9c33-21efe308c1c2)

Menghapus data villa <br>
`http://localhost:8080/villas/{id}`
![image](https://github.com/user-attachments/assets/a526beb1-786c-4e9b-b019-212b398d5518)

---

### 🔍 GET in Customers
Menampilkan daftar semua customer <br>
`http://localhost:8080/customers`
![image](https://github.com/user-attachments/assets/abf25bac-0ed2-4a0f-a15c-0d2c12d639cb)

Menampilkan informasi detail seorang customer <br>
`http://localhost:8080/customers/{id}`
![image](https://github.com/user-attachments/assets/0394092d-07e7-4459-a687-6dd73cba704e)

Menampilkan daftar booking yang telah dilakukan oleh seorang customer <br>
`http://localhost:8080/customers/{id}/bookings`
![image](https://github.com/user-attachments/assets/fac8ba48-3497-4fac-932d-9fd22d25ca2e)

Menampilkan daftar ulasan yang telah diberikan oleh customer <br>
`http://localhost:8080/customers/{id}/reviews`
![image](https://github.com/user-attachments/assets/12a07e64-363c-4236-90e5-3814b24119ef)

### ➕ POST in Customers
Menambahkan customer baru (registrasi customer) <br>
`http://localhost:8080/customers`
![image](https://github.com/user-attachments/assets/3344d4b3-ec1c-4714-8d7b-2a39e265f932)

Customer melakukan pemesanan villa <br>
`http://localhost:8080/customers/{id}/bookings`
![image](https://github.com/user-attachments/assets/85177533-235c-436a-aa2e-15a269753616)

Customer memberikan ulasan pada villa (berdasarkan informasi booking) <br>
`http://localhost:8080/customers/{id}/bookings/{id}/reviews`
![image](https://github.com/user-attachments/assets/edd15aca-1ec1-4592-b16f-e774ff31a61b)

### ✏️ PUT in Customers
Mengubah data seorang customer <br>
`http://localhost:8080/customers/{id}`
![image](https://github.com/user-attachments/assets/278623d5-e9b3-4d27-a78f-7676dd951d4c)

---

### 🔍 GET in Vouchers
Menampilkan daftar semua voucher <br>
`http://localhost:8080/vouchers`
![image](https://github.com/user-attachments/assets/cfbb1542-3f81-4101-9f75-a76c1a58ee48)

Menampilkan informasi detail suatu voucher <br>
`http://localhost:8080/vouchers/{id}`
![image](https://github.com/user-attachments/assets/983f1804-3ef6-4769-a842-2a229e4b6f84)

### ➕ POST in Vouchers
Membuat voucher baru <br>
`http://localhost:8080/vouchers`
![Screenshot 2025-07-03 181907](https://github.com/user-attachments/assets/3a94304e-b90d-446d-852f-38c5ee811dfe)

### ✏️ PUT in Vouchers
Mengubah data suatu voucher <br>
`http://localhost:8080/vouchers/{id}`
![image](https://github.com/user-attachments/assets/76b749e7-32f9-45e1-a0f1-c88acb79bfd9)

### 🗑️ DELETE in Vouchers
Menghapus data suatu voucher <br>
`http://localhost:8080/vouchers/{id}`
![image](https://github.com/user-attachments/assets/2518cddb-90fb-4b95-b1ce-70178e22a34f)

---

### 🛑 Error Response 400
Ketika membuat entitas baru (seperti melakukan **POST** pada villa), namun terdapat data yang tidak lengkap <br>
`http://localhost:8080/villas`
![Screenshot 2025-07-01 194057](https://github.com/user-attachments/assets/fc809648-a451-4632-b6f3-b939ee4d8841)

Ketika membuat customer, namun data nomor telepon (phone) tidak lengkap <br>
`http://localhost:8080/customers`
![Screenshot 2025-07-01 105715](https://github.com/user-attachments/assets/56a46b09-2590-4626-af4b-5c8cef0e4ba2)

Ketika membuat customer, namun data email tidak lengkap <br>
`http://localhost:8080/customers`
![Screenshot 2025-07-01 110028](https://github.com/user-attachments/assets/fbf22807-9e3d-439c-ba9d-39307f71bed4)

Ketika user mengakses endpoint `/villas/{id}` dengan ID yang tidak valid atau tidak sesuai data yang tersedia (seperti `/villas/test`), maka sistem akan merespons dengan error response 400 dan pesan "Invalid villa ID" <br>
`http://localhost:8080/villas/test`
![Screenshot 2025-07-04 022351](https://github.com/user-attachments/assets/834554a4-65ec-4ab6-8c10-ad177e234309)

### ❌ Error Response 404
Ketika melihat informasi detail suatu entitas (seperti pada villa), tetapi ID entitasnya tidak ada <br>
`http://localhost:8080/villas/{id}`
![Screenshot 2025-07-01 101736](https://github.com/user-attachments/assets/50eb4253-dd50-497d-a5f0-c0b4495e8801)

Ketika melakukan perubahan data pada entitas (seperti melakukan **DELETE**), tetapi entitasnya tidak ada <br>
`http://localhost:8080/villas/{id}`
![Screenshot 2025-06-30 171649](https://github.com/user-attachments/assets/8500e76e-065a-4054-83f3-e5efb2be4e0d)

Ketika user mengakses endpoint yang tidak tersedia (seperti `/test`), maka sistem akan merespons dengan error 404 bahwa endpoint tidak ditemukan <br>
`http://localhost:8080/test`
![Screenshot 2025-07-04 001919](https://github.com/user-attachments/assets/4c12740d-fc41-4a2f-9b94-d139833ec4bc)

### 🚫 Error Response 405
Melakukan method selain **GET**, **POST**, **PUT**, dan **DELETE** (seperti melakukan **PATCH**) <br>
`http://localhost:8080/villas/{id}`
![Screenshot 2025-06-30 173009](https://github.com/user-attachments/assets/2634c69e-9035-4535-be38-0f730733c286)

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
