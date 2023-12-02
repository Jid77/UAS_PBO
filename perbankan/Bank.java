package perbankan;

class Bank {
    private String namaBank;
    private String nomorRekening;

    public Bank(String namaBank, String nomorRekening) {
        this.namaBank = namaBank;
        this.nomorRekening = nomorRekening;
    }

    public String getNamaBank() {
        return namaBank;
    }

    public String getNomorRekening() {
        return nomorRekening;
    }
}
