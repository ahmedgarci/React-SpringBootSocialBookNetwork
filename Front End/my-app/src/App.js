import { AuthPage } from "./Pages/AuthenticationPage/AuthPage";
import { AllBooks } from "./Pages/Books/AllBooksPage";
import { HomePage } from "./Pages/Home/HomePage";
import {BrowserRouter as Router , Route, Routes} from "react-router-dom"
import { NavbarComponent } from "./Pages/NavBar/NavbarComponent";
function App() {
  return (
     <Router>
      <NavbarComponent/>
      <Routes>
        <Route path="/" Component={HomePage}/>
        <Route path="/login" Component={AuthPage}/>
        <Route path="/books" Component={AllBooks}/>
      </Routes>
    </Router>
   
  );
}

export default App;
