import React from 'react';
import '../App.css';

const LanguageSelector = () => {  // Объявляем компонент

    const handleLanguageChange = (event) => {
        const selectedLanguage = event.target.value;
        if (selectedLanguage === 'en') {
            window.location.href = `${window.location.pathname}?lang=en`; // Принудительно добавляем параметр для англ. языка
        } else {
            window.location.href = `${window.location.pathname}?lang=${selectedLanguage}`;
        }
    };

    return (
        <div className="language-selector">
            <label htmlFor="language"></label>
            <select id="language" onChange={handleLanguageChange}>
                <option value="en">English</option>
                <option value="de">Deutsch</option>
                <option value="tr">Türkçe</option>
                <option value="ru">Русский</option> {/* Добавлено */}
            </select>
        </div>
    );
};

export default LanguageSelector;
