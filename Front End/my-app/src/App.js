import { AuthPage } from "./Pages/AuthenticationPage/AuthPage";
import { HomePage } from "./Pages/Home/HomePage";
import {BrowserRouter as Router , Route, Routes} from "react-router-dom"
function App() {
  return (
    <Router>
      <Routes>
        <Route path="/" Component={HomePage}/>
        <Route path="/login" Component={AuthPage}/>
        
      </Routes>
    </Router>
  );
}

export default App;
