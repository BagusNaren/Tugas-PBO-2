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
