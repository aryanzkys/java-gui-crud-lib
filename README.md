# java-gui-crud-lib

## 1. Identitas Proyek

**Mata Kuliah**: Pemrograman Lanjut  
**Jenis Tugas**: Latihan Kerja 07 (LK07)  
**Judul**: Aplikasi Java GUI CRUD Data Siswa Berbasis File CSV  
**Tujuan**: Menerapkan pemrograman Java GUI untuk pemrosesan data file dengan operasi `CREATE`, `READ`, `UPDATE`, dan `DELETE`.

## 2. Ringkasan Program

Program ini merupakan aplikasi desktop berbasis Java Swing untuk mengelola data siswa perpustakaan SMP.
Data yang diproses terdiri dari:
- `NIS` (String)
- `Nama Siswa` (String)
- `Alamat` (String)

Seluruh data disimpan persisten pada berkas `siswa.csv`.

## 3. Mekanisme Program

### 3.1 Alur Eksekusi
1. Program dijalankan melalui kelas `Main`.
2. `StudentCrudFrame` dibuka sebagai antarmuka utama.
3. Saat startup, data dibaca dari `siswa.csv` lalu ditampilkan pada tabel.
4. Pengguna melakukan operasi CRUD melalui form input dan tombol aksi.
5. Setiap perubahan data disimpan kembali ke `siswa.csv`.

### 3.2 Mekanisme Validasi
- Seluruh field (`NIS`, `Nama`, `Alamat`) wajib terisi.
- `NIS` harus unik pada operasi `CREATE` maupun `UPDATE`.
- Jika terjadi duplikasi `NIS`, sistem menampilkan peringatan.

### 3.3 Mekanisme Penanganan Exception
- Jika `siswa.csv` tidak ditemukan saat startup:
  - Sistem menampilkan informasi peringatan.
  - Sistem membuat file kosong secara otomatis.
- Jika gagal membaca/menulis file:
  - Sistem menampilkan pesan galat dan membatalkan penyimpanan.

## 4. Fitur Program

1. **Struktur frame 4 bagian**
	- Judul program
	- Panel entri data
	- Panel tombol CRUD
	- Tabel data siswa
2. **READ otomatis saat startup** dari file CSV.
3. **CREATE data siswa baru** dengan validasi `NIS` unik.
4. **UPDATE data siswa** terpilih pada tabel.
5. **DELETE data siswa** dengan konfirmasi penghapusan.
6. **Sinkronisasi file**: seluruh operasi CRUD memperbarui `siswa.csv`.

## 5. Arsitektur dan Struktur Folder

```text
java-gui-crud-lib/
├─ src/
│  └─ lk07/
│     ├─ Main.java
│     ├─ Student.java
│     ├─ CsvStudentRepository.java
│     └─ StudentCrudFrame.java
├─ siswa.csv
└─ README.md
```

### Penjelasan Komponen
- `Main.java`: titik masuk aplikasi dan inisialisasi antarmuka.
- `Student.java`: model data siswa.
- `CsvStudentRepository.java`: pengelolaan I/O file CSV (baca/tulis).
- `StudentCrudFrame.java`: implementasi GUI, aksi CRUD, validasi, dan interaksi tabel.

## 6. Kebutuhan Sistem

- Java Development Kit (JDK) 17 atau lebih baru.
- Sistem operasi: Windows/Linux/macOS.
- Editor yang direkomendasikan: VS Code.

## 7. Prosedur Build dan Run

### 7.1 Melalui Terminal
1. Kompilasi:
	- `javac -d out src/lk07/*.java`
2. Jalankan aplikasi:
	- `java -cp out lk07.Main`

### 7.2 Melalui VS Code
- Buka `Main.java`, lalu jalankan menggunakan tombol **Run**.

## 8. Skenario Uji dan Bukti Output

Dokumentasi screenshot minimal yang disertakan pada laporan PDF:
1. Tampilan awal aplikasi (data tampil saat startup).
2. Hasil `CREATE` berhasil.
3. Validasi `CREATE` gagal karena `NIS` duplikat.
4. Hasil `UPDATE` berhasil.
5. Hasil `DELETE` berhasil.

## 9. Distribusi Tugas Tim 

**Koordinator Tim**: ARYAN ZAKY PRAYOGO (255150207111059)

Pembagian tugas dibuat merata berdasarkan porsi inti pengembangan:

1. **ARYAN ZAKY PRAYOGO** (Koordinator)
	- Koordinasi timeline dan integrasi akhir.
	- Review konsistensi fitur terhadap rubrik penilaian.

2. **ACHMAD HUJAIRI**
	- Implementasi struktur GUI (frame, panel, komponen input, tabel).
	- Penataan tampilan dan interaksi form.

3. **M. HIDAYATULLOH H. A. M**
	- Implementasi logika File I/O pada `CsvStudentRepository`.
	- Penanganan exception file (file tidak ada, baca/tulis gagal).

4. **M. AHSHAL ZILHAMSYAH**
	- Implementasi logika `CREATE`, `READ`, `UPDATE`, `DELETE`.
	- Implementasi validasi `NIS` unik dan validasi input.

5. **DIKARDO SIAHAAN**
	- Pengujian fungsional tiap fitur CRUD.
	- Penyusunan dokumentasi hasil uji dan kompilasi laporan PDF.

## 10. Data Anggota Kelompok

- ARYAN ZAKY PRAYOGO — 255150207111059
- ACHMAD HUJAIRI — 255150200111042
- M. HIDAYATULLOH H. A. M — 255150201111025
- M. AHSHAL ZILHAMSYAH — 255150200111041
- DIKARDO SIAHAAN — 255150200111040


