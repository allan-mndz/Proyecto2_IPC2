import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

@Injectable({
  providedIn: 'root',
})

export class Auth {

  private apiUrl = 'http://localhost:8080/api/login';
  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<any> {
    const body = { username, password };

    return this.http.post<any>(this.apiUrl, body).pipe(
      tap(response => {
        if (response == 'success') {
          localStorage.setItem('token', response.token);
          localStorage.setItem('usuario', JSON.stringify(response.usuario));
        }
      })
    );
  }

  logout(): void {
    localStorage.removeItem('token');
    localStorage.removeItem('usuario');
  }

  registrar(datosUsuario: any): Observable<any> {
    return this.http.post('http://localhost:8080/api/registro', datosUsuario);
  }
}
