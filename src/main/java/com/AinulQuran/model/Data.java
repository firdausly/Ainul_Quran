package com.AinulQuran.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;


public class Data {

        private String identifier;

        private String language;

        private String name;

        private String englishName;

        private String format;

        private String type;

        private String direction;

        public String getIdentifier() {
                return identifier;
        }

        public void setIdentifier(String identifier) {
                this.identifier = identifier;
        }

        public String getLanguage() {
                return language;
        }

        public void setLanguage(String language) {
                this.language = language;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public String getEnglishName() {
                return englishName;
        }

        public void setEnglishName(String englishName) {
                this.englishName = englishName;
        }

        public String getFormat() {
                return format;
        }

        public void setFormat(String format) {
                this.format = format;
        }

        public String getType() {
                return type;
        }

        public void setType(String type) {
                this.type = type;
        }

        public String getDirection() {
                return direction;
        }

        public void setDirection(String direction) {
                this.direction = direction;
        }
}
