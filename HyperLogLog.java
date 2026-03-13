import java.util.Arrays;

public class HyperLogLog {

    private final int p; // Hassasiyet (precision)
    private final int m; // Kova sayısı (2^p)
    private final double alphaM; // Düzeltme katsayısı
    private final byte[] registers; // Her kova için ardışık sıfır sayısı

    public HyperLogLog(int p) {
        if (p < 4 || p > 16) throw new IllegalArgumentException("p 4 ile 16 arasında olmalıdır.");
        this.p = p;
        this.m = 1 << p;
        this.registers = new byte[m];
        this.alphaM = getAlphaM(m);
    }

    // 1. Yüksek Kaliteli Hash Fonksiyonu (MurmurHash3 benzeri basitleştirilmiş)
    private long hash(Object item) {
        long h = item.hashCode();
        h ^= (h >>> 33);
        h *= 0xff51afd7ed558ccdL;
        h ^= (h >>> 33);
        h *= 0xc4ceb9fe1a85ec53L;
        h ^= (h >>> 33);
        return h;
    }

    // 2. Veri Ekleme ve Bucketing
    public void add(Object item) {
        long x = hash(item);
        int j = (int) (x >>> (64 - p)); // İlk p bit kova endeksini belirler
        long w = x << p; // Geri kalan bitler
        byte rho = (byte) (Long.numberOfLeadingZeros(w | (1L << (p - 1))) + 1);

        registers[j] = (byte) Math.max(registers[j], rho);
    }

    // 3. Tahmin Hesaplama (Harmonik Ortalama)
    public long estimate() {
        double sum = 0;
        for (byte register : registers) {
            sum += Math.pow(2, -register);
        }

        double estimate = alphaM * m * m * (1.0 / sum);

        // Küçük veri seti düzeltmesi (Linear Counting)
        if (estimate <= 2.5 * m) {
            int v = 0;
            for (byte r : registers) if (r == 0) v++;
            if (v != 0) return Math.round(m * Math.log((double) m / v));
        }

        return Math.round(estimate);
    }

    // 4. Birleştirilebilir (Mergeable) Özelliği
    public void merge(HyperLogLog other) {
        if (this.p != other.p) throw new IllegalArgumentException("Precision değerleri aynı olmalı!");
        for (int i = 0; i < m; i++) {
            this.registers[i] = (byte) Math.max(this.registers[i], other.registers[i]);
        }
    }

    private double getAlphaM(int m) {
        switch (m) {
            case 16: return 0.673;
            case 32: return 0.697;
            case 64: return 0.709;
            default: return 0.7213 / (1 + 1.079 / m);
        }
    }

    public static void main(String[] args) {
        HyperLogLog hll = new HyperLogLog(12); // 2^12 = 4096 kova

        // Test verisi ekleme
        for (int i = 0; i < 100000; i++) {
            hll.add("user_" + i);
        }

        System.out.println("Gerçek Sayı: 100,000");
        System.out.println("HLL Tahmini: " + hll.estimate());
    }
}