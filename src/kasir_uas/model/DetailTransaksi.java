package kasir_uas.model;


public class DetailTransaksi {
    private String kodeBarang;
    private int harga;
    private int jumlah;
    private int subTotal;

    public DetailTransaksi(String kodeBarang, int harga, int jumlah, int subTotal) {
        this.kodeBarang = kodeBarang;
        this.harga = harga;
        this.jumlah = jumlah;
        this.subTotal = subTotal;
    }

    public String getKodeBarang() {
        return kodeBarang;
    }

    public int getHarga() {
        return harga;
    }

    public int getJumlah() {
        return jumlah;
    }
    
    public int getSubTotal() {
        return subTotal;
    }
}
