package kasir_uas.model;


public class Barang {
    private String idBarang;
    private String namaBarang;
    private String satuan;
    private int stok;
    private double harga;

    public Barang() {}

    public Barang(String idBarang, String namaBarang, String satuan, int stok, double harga) {
        this.idBarang = idBarang;
        this.namaBarang = namaBarang;
        this.satuan = satuan;
        this.stok = stok;
        this.harga = harga;
    }

    // Getter dan Setter
    public String getIdBarang() { return idBarang; }
    public void setIdBarang(String idBarang) { this.idBarang = idBarang; }

    public String getNamaBarang() { return namaBarang; }
    public void setNamaBarang(String namaBarang) { this.namaBarang = namaBarang; }

    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }

    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    public double getHarga() { return harga; }
    public void setHarga(double harga) { this.harga = harga; }

}

