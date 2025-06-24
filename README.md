# Tugas-PBO-2

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

Tugas ini adalah backend API sederhana yang dibangun menggunakan **Java** tanpa framework eksternal dan menggunakan arsitektur modular berbasis **Handler-DAO-Model**. API ini berfungsi untuk mengelola data berbagai entitas seperti **Villa**, **Customer**, **Booking**, **Voucher**, dan lainnya. API ini mendukung metode HTTP **GET**, **POST**, **PUT**, dan **DELETE**, serta mengembalikan response dalam format **JSON**.

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

#### ➤ `Tugas2`
Berisi komponen utama untuk menjalankan server HTTP dan menangani komunikasi client-server:
- `Request` – Untuk parsing HTTP request dari client.
- `Response` – Untuk membentuk HTTP response ke client.
- `Server` – Untuk menjalankan web server lokal pada port `8080`.
- `Tugas2` – Class utama untuk mengeksekusi aplikasi.

---
