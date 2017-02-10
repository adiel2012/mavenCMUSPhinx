/*
 * Copyright 2014 Carnegie Mellon University.
 * All Rights Reserved.  Use is subject to license terms.
 *
 * See the file "license.terms" for information on usage and
 * redistribution of this file, and for a DISCLAIMER OF ALL
 * WARRANTIES.
 */

package com.mycompany.mavencmusphinx;

import java.io.InputStream;

import edu.cmu.sphinx.api.Configuration;
import edu.cmu.sphinx.api.Context;
import edu.cmu.sphinx.api.SpeechResult;
import edu.cmu.sphinx.recognizer.Recognizer;
import edu.cmu.sphinx.result.Result;
import edu.cmu.sphinx.result.WordResult;
import edu.cmu.sphinx.speakerid.SpeakerIdentification;
import edu.cmu.sphinx.util.TimeFrame;
import java.io.File;
import java.net.URL;

/**
 * A simple example that shows how to transcribe a continuous audio file that
 * has multiple utterances in it.
 */
public class AllphoneDemo {

      private static String root = System.getProperty("user.dir");
    private static final String ACOUSTIC_MODEL = "file:"+root+"\\models\\en-us\\en-us";
//        "resource:/edu/cmu/sphinx/models/en-us/en-us";
    private static final String DICTIONARY_PATH = "file:"+root+"\\models\\en-us\\cmudict-en-us.dict";
//        "resource:/edu/cmu/sphinx/models/en-us/cmudict-en-us.dict";
    private static final String GRAMMAR_PATH = "file:"+root+"\\demo\\dialog";
//        "resource:/resources/";
    private static final String LANGUAGE_MODEL =
        "file:"+root+"\\demo\\dialog\\weather.lm";
    
    public static void main(String[] args) throws Exception {
        
        
        
        System.out.println("Loading models...");

        Configuration configuration = new Configuration();

        // Load model from the jar
        configuration
                .setAcousticModelPath(ACOUSTIC_MODEL);

        // You can also load model from folder
        // configuration.setAcousticModelPath("file:en-us");

        configuration
                .setDictionaryPath(DICTIONARY_PATH);
        Context context = new Context(configuration);
        context.setLocalProperty("decoder->searchManager", "allphoneSearchManager");
        Recognizer recognizer = context.getInstance(Recognizer.class);
        SpeakerIdentification sd = new SpeakerIdentification();
        File f = new File(root+"\\test.wav");
        System.out.println(f.exists());
        URL url = f.toURI().toURL();
        InputStream stream = url.openStream();
//                AllphoneDemo.class
//                .getResourceAsStream("/edu/cmu/sphinx/demo/aligner/10001-90210-01803.wav");
        stream.skip(44);

        // Simple recognition with generic model
        recognizer.allocate();
        context.setSpeechSource(stream, TimeFrame.INFINITE);
        Result result;
        while ((result = recognizer.recognize()) != null) {
            SpeechResult speechResult = new SpeechResult(result);
            System.out.format("Hypothesis: %s\n", speechResult.getHypothesis());

            System.out.println("List of recognized words and their times:");
            for (WordResult r : speechResult.getWords()) {
                System.out.println(r);
            }
        }
        recognizer.deallocate();

    }
}
