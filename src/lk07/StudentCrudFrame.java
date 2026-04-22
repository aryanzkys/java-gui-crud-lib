package lk07;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;

public class StudentCrudFrame extends JFrame {
    private final CsvStudentRepository repository;

    private final JTextField nisField = new JTextField(20);
    private final JTextField namaField = new JTextField(20);
    private final JTextField alamatField = new JTextField(20);

    private final DefaultTableModel tableModel = new DefaultTableModel(
            new Object[]{"NIS", "Nama Siswa", "Alamat"}, 0
    ) {
        @Override
        public boolean isCellEditable(int row, int column) {
            return false;
        }
    };

    private final JTable studentTable = new JTable(tableModel);
    private final List<Student> students = new ArrayList<>();

    public StudentCrudFrame(Path csvPath) {
        this.repository = new CsvStudentRepository(csvPath);

        setTitle("LK07 - CRUD Data Siswa Perpustakaan");
        setSize(860, 560);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        initComponents();
        loadDataAtStartup();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));

        JPanel titlePanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("APLIKASI CRUD DATA SISWA (NIS, NAMA, ALAMAT)", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 18));
        titlePanel.setBorder(BorderFactory.createEmptyBorder(14, 12, 6, 12));
        titlePanel.add(titleLabel, BorderLayout.CENTER);

        JPanel inputPanel = createInputPanel();
        JPanel buttonPanel = createButtonPanel();
        JPanel northContainer = new JPanel(new BorderLayout());
        northContainer.add(titlePanel, BorderLayout.NORTH);
        northContainer.add(inputPanel, BorderLayout.CENTER);
        northContainer.add(buttonPanel, BorderLayout.SOUTH);

        JScrollPane tableScroll = new JScrollPane(studentTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder("Tabel Data Siswa"));
        studentTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        studentTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && studentTable.getSelectedRow() >= 0) {
                fillFormFromSelectedRow(studentTable.getSelectedRow());
            }
        });

        add(northContainer, BorderLayout.NORTH);
        add(tableScroll, BorderLayout.CENTER);
    }

    private JPanel createInputPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createTitledBorder("Entri Data"));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 6, 6, 6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("NIS"), gbc);

        gbc.gridx = 1;
        panel.add(nisField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(new JLabel("Nama Siswa"), gbc);

        gbc.gridx = 1;
        panel.add(namaField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(new JLabel("Alamat"), gbc);

        gbc.gridx = 1;
        panel.add(alamatField, gbc);

        return panel;
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createTitledBorder("Tombol CRUD"));

        JButton createButton = new JButton("CREATE");
        JButton readButton = new JButton("READ");
        JButton updateButton = new JButton("UPDATE");
        JButton deleteButton = new JButton("DELETE");
        JButton clearButton = new JButton("CLEAR FORM");

        createButton.addActionListener(e -> createStudent());
        readButton.addActionListener(e -> refreshTable());
        updateButton.addActionListener(e -> updateStudent());
        deleteButton.addActionListener(e -> deleteStudent());
        clearButton.addActionListener(e -> clearForm());

        panel.add(createButton);
        panel.add(readButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);

        return panel;
    }

    private void loadDataAtStartup() {
        try {
            students.clear();
            students.addAll(repository.loadAll());
            refreshTableViewOnly();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Berkas siswa.csv belum ada. Berkas kosong akan dibuat otomatis.\n" + ex.getMessage(),
                    "Informasi File",
                    JOptionPane.WARNING_MESSAGE
            );

            try {
                repository.createEmptyFile();
            } catch (IOException ioException) {
                JOptionPane.showMessageDialog(
                        this,
                        "Gagal membuat berkas siswa.csv: " + ioException.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal membaca data siswa.csv: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    private void createStudent() {
        String nis = nisField.getText().trim();
        String nama = namaField.getText().trim();
        String alamat = alamatField.getText().trim();

        if (!validateInput(nis, nama, alamat)) {
            return;
        }

        if (findIndexByNis(nis) >= 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "NIS sudah digunakan. NIS harus unik.",
                    "Validasi NIS",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        students.add(new Student(nis, nama, alamat));

        if (!persistAndRefresh("Data siswa berhasil ditambahkan.")) {
            students.remove(students.size() - 1);
        }
    }

    private void refreshTable() {
        loadDataAtStartup();
        JOptionPane.showMessageDialog(
                this,
                "Data pada tabel sudah diperbarui dari file siswa.csv.",
                "READ",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    private void updateStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data pada tabel yang akan di-update.",
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        String oldNis = students.get(selectedRow).getNis();
        String newNis = nisField.getText().trim();
        String nama = namaField.getText().trim();
        String alamat = alamatField.getText().trim();

        if (!validateInput(newNis, nama, alamat)) {
            return;
        }

        int duplicateIndex = findIndexByNis(newNis);
        if (duplicateIndex >= 0 && duplicateIndex != selectedRow) {
            JOptionPane.showMessageDialog(
                    this,
                    "NIS sudah digunakan data lain. Gunakan NIS yang berbeda.",
                    "Validasi NIS",
                    JOptionPane.WARNING_MESSAGE
            );
            return;
        }

        Student edited = students.get(selectedRow);
        edited.setNis(newNis);
        edited.setNama(nama);
        edited.setAlamat(alamat);

        if (!persistAndRefresh("Data siswa dengan NIS " + oldNis + " berhasil di-update.")) {
            loadDataAtStartup();
        }
    }

    private void deleteStudent() {
        int selectedRow = studentTable.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(
                    this,
                    "Pilih data pada tabel yang akan dihapus.",
                    "Informasi",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return;
        }

        Student toDelete = students.get(selectedRow);
        int confirm = JOptionPane.showConfirmDialog(
                this,
                "Yakin hapus data dengan NIS " + toDelete.getNis() + "?",
                "Konfirmasi Hapus",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE
        );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        students.remove(selectedRow);

        if (!persistAndRefresh("Data siswa berhasil dihapus.")) {
            students.add(selectedRow, toDelete);
        }
    }

    private boolean validateInput(String nis, String nama, String alamat) {
        if (nis.isEmpty() || nama.isEmpty() || alamat.isEmpty()) {
            JOptionPane.showMessageDialog(
                    this,
                    "NIS, Nama Siswa, dan Alamat wajib diisi.",
                    "Validasi Input",
                    JOptionPane.WARNING_MESSAGE
            );
            return false;
        }

        return true;
    }

    private int findIndexByNis(String nis) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getNis().equals(nis)) {
                return i;
            }
        }

        return -1;
    }

    private boolean persistAndRefresh(String successMessage) {
        try {
            repository.saveAll(students);
            refreshTableViewOnly();
            clearForm();

            JOptionPane.showMessageDialog(
                    this,
                    successMessage,
                    "Sukses",
                    JOptionPane.INFORMATION_MESSAGE
            );
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(
                    this,
                    "Gagal menyimpan perubahan ke siswa.csv: " + ex.getMessage(),
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
            return false;
        }
    }

    private void refreshTableViewOnly() {
        tableModel.setRowCount(0);
        for (Student student : students) {
            tableModel.addRow(new Object[]{student.getNis(), student.getNama(), student.getAlamat()});
        }
    }

    private void fillFormFromSelectedRow(int rowIndex) {
        Student student = students.get(rowIndex);
        nisField.setText(student.getNis());
        namaField.setText(student.getNama());
        alamatField.setText(student.getAlamat());
    }

    private void clearForm() {
        nisField.setText("");
        namaField.setText("");
        alamatField.setText("");
        studentTable.clearSelection();
        SwingUtilities.invokeLater(nisField::requestFocusInWindow);
    }
}
