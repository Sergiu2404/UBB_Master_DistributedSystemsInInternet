java Split1 t.zip 100000 >>out.txt
java Merge1 x >>out.txt
fc t.zip x >>out.txt
java SplitB t.zip 100000 >>out.txt
java MergeB y >>out.txt
fc t.zip y >>out.txt
java SplitThr t.zip 100000 >>out.txt
java MergeThr z >>out.txt
fc t.zip z >>out.txt
