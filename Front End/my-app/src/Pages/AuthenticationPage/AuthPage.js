import { useState } from "react"
import { NavbarComponent } from "../NavBar/NavbarComponent";
import { login } from "../../Requests/AuthRequest/AuthRequest";

function AuthPage(){
    const [notRegisteredYet,setNotRegistered]= useState(false);
    const [AuthRequest,setAuthRequest]=useState({email:null,password:null})

    return(
      <>
      <NavbarComponent/>
        <div className="bg-gray-100 flex justify-center items-center min-h-screen">
        <div class="bg-white shadow-md rounded-lg p-8 max-w-md min-h-full">
        <h2 class="text-2xl font-bold text-center mb-6 text-gray-800">Welcome to SocialBook</h2>
        <form>          
        {notRegisteredYet && (
            <>
         <div class="mb-6" >
         <label class="block text-gray-700 text-sm font-bold mb-2" for="password">
           First name
         </label>
         <input 
        id="firstname" type="text" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
        placeholder="Enter your first name"
        onChange={(e)=>{setAuthRequest({...AuthRequest,email:e.target.value})}}  
          />
       </div>
       
       <div class="mb-6">
         <label class="block text-gray-700 text-sm font-bold mb-2" for="lastname">
         Last name 
         </label>
         <input id="lastname" type="text" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline"
          placeholder="Enter your last name"
          onChange={(e)=>{setAuthRequest({...AuthRequest,password:e.target.value})}}  

          />
       </div>     
       </>
        )
        }
       

            <div class="mb-4">
                <label class="block text-gray-700 text-sm font-bold mb-2" for="email">
          Email
        </label>
        <input id="email" type="email" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 leading-tight focus:outline-none focus:shadow-outline" placeholder="Enter your email"/>
      </div>

      <div class="mb-6">
        <label class="block text-gray-700 text-sm font-bold mb-2" for="password">
          Password
        </label>
        <input id="password" type="password" class="shadow appearance-none border rounded w-full py-2 px-3 text-gray-700 mb-3 leading-tight focus:outline-none focus:shadow-outline" placeholder="Enter your password"/>
      </div>

      
     

      <div class="flex items-center justify-between">
        <button class="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded focus:outline-none focus:shadow-outline w-full"
         type="submit"
         onClick={()=>login({email:AuthRequest.email,password:AuthRequest.password})}
         >
          Sign In
        </button>
      </div>
    </form>



    <p class="mt-6 text-center text-gray-600">
      Don't have an account? <a href="#"
      onClick={()=>setNotRegistered(!notRegisteredYet)}
      class="text-blue-500 font-bold hover:underline">Sign up</a>
    </p>
  </div>
  </div>
  </>
    )
}

export{AuthPage}