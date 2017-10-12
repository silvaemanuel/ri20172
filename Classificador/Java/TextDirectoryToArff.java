package classification;

import weka.core.*;
import weka.core.converters.*;
import weka.classifiers.trees.*;
import weka.filters.*;
import weka.filters.unsupervised.attribute.*;

import java.io.*;

public class TextDirectoryToArff {

  public static void main(String[] args) throws Exception {

    TextDirectoryLoader loader = new TextDirectoryLoader();
    loader.setDirectory(new File("C:/Users/Eduardo/eclipse-workspace/TesteJava/data/Txt/"));

    Instances dataRaw = loader.getDataSet();
    System.out.println("\n\nImported data:\n\n" + dataRaw);
    		 ArffSaver saver = new ArffSaver();
    		 saver.setInstances(dataRaw);
    		 saver.setFile(new File("./data/test.arff"));
    		 saver.writeBatch();

    StringToWordVector filter = new StringToWordVector();
    filter.setInputFormat(dataRaw);
    Instances dataFiltered = Filter.useFilter(dataRaw, filter);
    System.out.println("\n\nFiltered data:\n\n" + dataFiltered);
    ArffSaver saver2 = new ArffSaver();
    saver2.setInstances(dataFiltered);
    saver2.setFile(new File("./data/testFilter.arff"));
    saver2.writeBatch();

  }
}
