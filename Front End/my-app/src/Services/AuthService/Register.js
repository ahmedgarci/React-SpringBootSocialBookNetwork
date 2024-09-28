const API_URL = 'http:/localhost:4200'; 

export const Register = async ({email,password,firstname,lastname}) => {
    const response = await fetch(`${API_URL}/auth/register`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password , firstname , lastname }),
  });

  if (!response.ok) {
    throw new Error('Erreur lors de la connexion');
  }

  const data = await response.json();
  return data; 
};


