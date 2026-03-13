
Algoritma Analizi ve Tasarımı Final Degerlendirme Ödevi-2
HyperLogLog (HLL)

Bu proje, büyük veri analitiğinde benzersiz öğe sayısını (cardinality estimation) minimum bellek kullanımıyla tahmin etmek için kullanılan HyperLogLog (HLL) algoritmasının Java ile gerçeklenmesini içermektedir.

 Proje Hakkında
Geleneksel veri yapılarının (HashSet vb.) milyarlarca veriyi sayarken karşılaştığı bellek darboğazına çözüm olarak geliştirilen HLL, olasılıksal bir yaklaşım sunar. 
Bu proje, Agentic Kodlama metodolojisi ile modüler bir yapıda tasarlanmış olup, teorik hata sınırları ve birleştirilebilirlik (mergeability) özelliklerini içermektedir.

Temel Özellikler
Yüksek Kaliteli Hashing: Düşük çakışma oranlı 64-bit hash fonksiyonu.
Dinamik Bucketing: Veriyi $m = 2^p$ adet kovaya ayıran mekanizma.
Harmonik Ortalama: Varyansı minimize eden tahminleme motoru.
Bias Correction: Küçük veri setleri için Linear Counting düzeltmesi.
Mergeability: İki farklı HLL yapısını veri kaybı olmadan birleştirme desteği.

