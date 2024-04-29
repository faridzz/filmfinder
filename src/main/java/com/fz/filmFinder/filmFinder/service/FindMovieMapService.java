package com.fz.filmFinder.filmFinder.service;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class FindMovieMapService {

    //اسم فیلم رو ورودی میگیره
    public Optional<Document> getMoviesHtml(String movieName) {
        try {
            // URL مورد نظر را تعیین کنید
            String url = "https://www.movie-map.com/" + movieName;

            // ارسال درخواست GET و دریافت صفحه HTML
            Document doc = Jsoup.connect(url).get();
            // نمایش محتوای صفحه HTML
//            log.info("HTML Content:");
//            log.info(doc.toString());


            return Optional.of(doc); // بازگرداندن Document به صورت Optional
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty(); // بازگرداندن خالی به عنوان نشانگر خطا
        }
    }
//get similar moviesList
    public List<String> moviesList(Optional<Document> html) {
        List<String> subElementsText = new ArrayList<>();
        try {
            Document document = html.get();
            // انتخاب عنصر اصلی با شناسه "gnodMap"
            Element gnodMap = document.selectFirst("#gnodMap");
            // انتخاب تمام عناصر زیرمجموعه با کلاس ".sub-element"
            Elements subElements = gnodMap.select(".S");
            // پیمایش عناصر زیرمجموعه و افزودن متن آنها به لیست
            for (Element subElement : subElements) {
                String movieName = subElement.text();
                subElementsText.add(movieName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("Success");
        log.info(subElementsText.toString());
        return subElementsText;
    }


}

