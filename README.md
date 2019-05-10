#Test 3 - Sort

Approach, split the file multiple buckets based on the value.
For each value
    Identify bucket for value, read bucket file
    Add value to the bucket
    write the bucket to file
    
Once the whole file has been read, merge all bucket files to the 1 file.

As each file is being already sorted, a basic append operation is only needed.


This is quite slow regarding the amount of IO that the reading/writing files is creating 2 * Number of lines

A faster approach would be to keep the bucket in memory,which would not fulfill the hardware requirements.

