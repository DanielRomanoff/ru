package ru.skillbench.tasks.javaapi.io;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class WordFinderImpl implements WordFinder {
    String text;
    Set<String> words;
    @Override
    public String getText() {
        return text;
    }

    @Override
    public void setText(String text) {
        if(text == null) throw new IllegalArgumentException();
        this.text = text;
    }

    @Override
    public void setInputStream(InputStream is) throws IOException {
        if(is == null) throw new IllegalArgumentException();
        byte[] buffer = new byte[is.available()];
        while(is.read() != -1){
            text = new String(buffer);
        }
    }

    @Override
    public void setFilePath(String filePath) throws IOException {
        if(filePath == null) throw new IllegalArgumentException();
        setInputStream(new FileInputStream(filePath));
    }

    @Override
    public void setResource(String resourceName) throws IOException {
        if(resourceName == null) throw new IllegalArgumentException();
        setInputStream(getClass().getResourceAsStream(resourceName));
    }

    @Override
    public Stream<String> findWordsStartWith(String begin) {
        if(text == null) throw new IllegalStateException();
        String notNullString = (begin==null) ? "" : begin;
        words = new HashSet<>(Arrays.asList(getText().split("\\s+")))
                .parallelStream()
                .filter(word -> word.regionMatches(true, 0, notNullString, 0, notNullString.length()))
                .map(String::toLowerCase)
                .collect(Collectors.toSet());
        return words.parallelStream();
    }

    @Override
    public void writeWords(OutputStream os) throws IOException {
        if(words == null) throw new IllegalStateException();
        Optional<String> stringOptional = words.parallelStream()
                .sorted()
                .reduce((first, second) -> first + " " + second);
        while(stringOptional.isPresent()){
            os.write(stringOptional.get().getBytes());
        }
    }
}
