import React from 'react';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom';
import Index from './components/Index';
import AnotherPage from './components/AnotherPage'; // если у тебя есть другие страницы

function App() {
    return (
        <Router>
            <Routes>
                <Route path="/" element={<Index />} />
                <Route path="/another-page" element={<AnotherPage />} /> {/* Пример другого маршрута */}
            </Routes>
        </Router>
    );
}

export default App;
