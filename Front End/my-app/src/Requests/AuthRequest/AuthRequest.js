const API_URL = ''; 

export const login = async ({email,password}) => {
    const response = await fetch(`${API_URL}/auth/login`, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    body: JSON.stringify({ email, password }),
  });

  if (!response.ok) {
    throw new Error('Erreur lors de la connexion');
  }

  const data = await response.json();
  return data; 
};


