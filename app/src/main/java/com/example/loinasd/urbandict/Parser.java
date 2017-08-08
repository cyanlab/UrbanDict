package com.example.loinasd.urbandict;
import android.support.annotation.NonNull;
import android.util.Log;

import org.jetbrains.annotations.Contract;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

import static com.example.loinasd.urbandict.MainActivity.ACT_TAG;
import static com.example.loinasd.urbandict.MainActivity.FULL_PAGE_TAG;

final class Parser{

    private ArrayList<Item> items = new ArrayList<>();
    //public StringBuilder fullPage = new StringBuilder(100000);
    public int countChars = 0;
    private static char currentChar;

    private final static String markerMeaning = "=\"meaning\"";
    private final static String markerExample = "=\"example\"";
    private final static String markerName = "=\"word\"";
    private final static String markerRibbon = "=\"ribbon\"";
    private static  int ch = 0;
    private  static int ribonCount = 0;

    private BufferedReader r;


    public void setBufferedReader(BufferedReader r) {
        this.r = r;
    }


    public ArrayList<Item> getItems() {
        return items;
    }

    void parse() throws IOException {

        ch = 0;

        while (hasMoreRibbons()) {
            ribonCount++;
            Item item = getNewItem();
            items.add(item);
            Log.i(ACT_TAG, item.getName());
            String str = "Ribbon: " + item.getRibbon() + "\nMeaning: " + item.getMeaning() + "\nExample: " + item.getExample();
            Log.i(ACT_TAG, str);
            if (ribonCount==3) {
                System.out.println();
            }
        }



        r.close();
       // Log.d(FULL_PAGE_TAG, fullPage.toString());
        Log.d(FULL_PAGE_TAG, String.valueOf(countChars));
    }

    private String readMarker(String marker, String stopTag) throws IOException {

        outer:   while (ch > -1) {
            ch = r.read();    currentChar = (char) ch;
            countChars++;
            //fullPage.append(currentChar);

            if (currentChar == marker.charAt(0)) {

                for (int i = 1; i < marker.length(); i++) {

                    if (i == 3)  {
                        System.out.println();
                    }

                    currentChar = (char) r.read();
                    countChars++;
                    //fullPage.append(currentChar);
                    if (currentChar != marker.charAt(i)) continue outer;

                }
                skipToTagEnd();
                return readText(stopTag);

            }
        }
        return null;
    }

    private char replaceSpecialSymbols(String symbol) {
        String[] symbols = new String[]{
                "&nbsp;",  //	\00A0	//Неразрывный пробел
                "&shy;",    //\00AD	    //Место возможного переноса
                "&lt;",    //\003C	    //Знак "меньше чем" (начало тега)
                "&gt;",    //\003E	    //Знак "больше чем" (конец тега)
                "&laquo;",    //\00AB	    //Левая двойная угловая скобка
                "&raquo;",    //\00BB	    //Правая двойная угловая скобка
                "&lsaquo;",    //\2039	    //Левая угловая одиночная кавычка
                "&rsaquo;",    //\203A	    //Правая угловая одиночная кавычка
                "&quot;",    //\0022	    //Двойная кавычка
                "&prime;",    //\2032	    //Одиночный штрих
                "&Prime;",    //\2033	    //Двойной штрих
                "&lsquo;",    //\2018	    //Левая одиночная кавычка
                "&rsquo;",        //\2019	    //Правая одиночная кавычка
                "&sbquo;",        //\201A	    //Нижняя одиночная кавычка
                "&ldquo;",    //\201C	    //Левая двойная кавычка
                "&rdquo;",    //\201D	    //Правая двойная кавычка
                "&bdquo;",        //\201E	    //Нижняя двойная кавычка
                "&#10076;",        //\275C	    //Жирная одинарная верхняя запятая
                "&#10075;",        //\275B	    //Жирная одинарная повёрнутая верхняя запятая
                "&amp;",        //\0026	    //Амперсанд
                "&apos;",    //\0027	    //Апостроф (одинарная кавычка)
                "&copy;",    //\00A9	    //Знак copyright
                "&reg;",        //\00AE	    //Знак зарегистрированной торговой марки
                "&acute;",    //\00B4	    //Знак ударения
                "&middot;",    //\00B7	    //Знак умножения
                "&iquest;",    //\00BF	    //Перевернутый вопросительный знак
                "&trade;",        //\2122	    //Знак торговой марки
                "&bull;",        //\2022	    //Маркер списка
                "&ndash;",    //\2013	    //Среднее тире
                "&mdash;",    //\2014	    //Длинное тире
                "&#125;",   	//\007D	    //Правая фигурная скобка
                "&#123;",	    //\007B	    //Левая фигурная скобка
                "&#61;",    	//\003D	    //Знак равенства
                "&ne;",     	//\2260	    //Знак неравенства
                "&#64;",    	//\0040	    //Символ собака
                "&#91;",    	//\005B	    //Левая квадратная скобка
                "&#93;"
        };

        char[] chars = new char[]{
                '\u00A0',   	//Неразрывный пробел
                '\u00AD',	    //Место возможного переноса
                '\u003C',	    //Знак "меньше чем" (начало тега)
                '\u003E',	    //Знак "больше чем" (конец тега)
                '\u00AB',	    //Левая двойная угловая скобка
                '\u00BB',	    //Правая двойная угловая скобка
                '\u2039',	    //Левая угловая одиночная кавычка
                '\u203A',	    //Правая угловая одиночная кавычка
                '\u0022',	    //Двойная кавычка
                '\u2032',	    //Одиночный штрих
                '\u2033',	    //Двойной штрих
                '\u2018',	    //Левая одиночная кавычка
                '\u2019',	    //Правая одиночная кавычка
                '\u201A',	    //Нижняя одиночная кавычка
                '\u201C',	    //Левая двойная кавычка
                '\u201D',	    //Правая двойная кавычка
                '\u201E',	    //Нижняя двойная кавычка
                '\u275C',	    //Жирная одинарная верхняя запятая
                '\u275B',	    //Жирная одинарная повёрнутая верхняя запятая
                '\u0026',	    //Амперсанд
                '\'',	    //Апостроф (одинарная кавычка)
                '\u00A9',	    //Знак copyright
                '\u00AE',	    //Знак зарегистрированной торговой марки
                '\u00B4',	    //Знак ударения
                '\u00B7',	    //Знак умножения
                '\u00BF',	    //Перевернутый вопросительный знак
                '\u2122',	    //Знак торговой марки
                '\u2022',	    //Маркер списка
                '\u2013',	    //Среднее тире
                '\u2014',	    //Длинное тире
                '\u007D',	    //Правая фигурная скобка
                '\u007B',
                '\u003D',
                '\u2260',
                '\u0040',
                '\u005B',
                '\u005D'

        };

        for (int i = 0; i < symbols.length; i++) {
            if (symbols[i].equals(symbol.trim())) {
                return chars[i];
            }
        }
        return ' ';
    }

    private String readTag() throws IOException {
        StringBuilder tg = new StringBuilder();
        while (currentChar != '>') {
            tg.append(currentChar);
            ch = r.read();    currentChar = (char) ch;
            countChars++;
            //fullPage.append(currentChar);
        }
        tg.append(currentChar);
        ch = r.read();    currentChar = (char) ch;
        countChars++;
        //fullPage.append(currentChar);

        return new String(tg);
    }

    private String readSpec() throws IOException {
        StringBuilder sp = new StringBuilder();
        sp.append(currentChar);
        while (currentChar != ';') {
            ch = r.read();    currentChar = (char) ch;
            countChars++;
            //fullPage.append(currentChar);
            sp.append(currentChar);
        }
        ch = r.read();    currentChar = (char) ch;
        countChars++;
        //fullPage.append(currentChar);
        return new String(sp);
    }

    private void skipToTagEnd() throws IOException {

        do {
            ch = r.read();    currentChar = (char) ch;
            countChars++;
            //fullPage.append(currentChar);
        } while (currentChar != '>');
    }

    private Item getNewItem() throws IOException {
        return new Item(readText("</div>"), readMarker(markerName, "</a>"),
                readMarker(markerMeaning, "</div>"), readMarker(markerExample, "</div>"));
    }

    private boolean hasMoreRibbons() throws IOException {
        outer:   while (ch > -1) {
            ch = r.read();    currentChar = (char) ch;
            countChars++;
            //fullPage.append(currentChar);
            if (currentChar == markerRibbon.charAt(0)) {

                for (int i = 1; i < markerRibbon.length(); i++) {

                    if (i == 4)  {
                        System.out.println();
                    }

                    currentChar = (char) r.read();
                    countChars++;
                    //fullPage.append(currentChar);
                    if (currentChar != markerRibbon.charAt(i)) continue outer;

                }
                skipToTagEnd();
                return true;
            }
        }
        return false;
    }

    private String readText(String stopTag) throws IOException {
        StringBuilder buf = new StringBuilder();
        ch = r.read();    currentChar = (char) ch;
        countChars++;
        //fullPage.append(currentChar);
        if (currentChar == '<') return "";

        boolean gotTagEnd = false;
        while (!gotTagEnd) {

            if (currentChar == '<') {
                String tag = readTag();
                if (tag.equals(stopTag)) gotTagEnd = true;
                else if (tag.equals("<br/>")) buf.append('\n');
            }
            if (currentChar == '&') {
                buf.append(replaceSpecialSymbols(readSpec()));
            } else if (!gotTagEnd){
                buf.append(currentChar);
                ch = r.read();    currentChar = (char) ch;
                countChars++;
                //fullPage.append(currentChar);
                if (ch == 13) ch = 10;
            }
        }
        return new String(buf);
    }


}
