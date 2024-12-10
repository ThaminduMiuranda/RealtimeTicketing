// src/app/services/web-socket.service.ts

import { Injectable } from '@angular/core';
// @ts-ignore
import SockJS from 'sockjs-client';
import { Observable, Subject } from 'rxjs';
// @ts-ignore
import {Client, IMessage, StompSubscription} from '@stomp/stompjs';

@Injectable({
  providedIn: 'root',
})
export class WebSocketService {
  private client: Client;
  private messageSubject: Subject<string> = new Subject<string>();

  constructor() {
    this.client = new Client({
      brokerURL: 'ws://localhost:8080/ws', // WebSocket endpoint
      connectHeaders: {
        // 'login': 'user',
        // 'passcode': 'password',
      },
      debug: function (str: any) {
        console.log(str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 0,
      heartbeatOutgoing: 20000,
      webSocketFactory: () => new SockJS('http://localhost:8080/ws'),
    });

    this.client.onConnect = () => {
      console.log('WebSocket connected');
      this.client.subscribe('/topic/simulation', (message: IMessage) => {
        this.messageSubject.next(message.body);
      });
    };

    this.client.onStompError = (frame: { headers: { [x: string]: string; }; body: string; }) => {
      console.error('Broker reported error: ' + frame.headers['message']);
      console.error('Additional details: ' + frame.body);
    };

    this.client.activate();
  }

  /**
   * Observable to receive messages
   */
  public onMessage(): Observable<string> {
    return this.messageSubject.asObservable();
  }

  /**
   * Disconnect the client
   */
  public disconnect(): void {
    this.client.deactivate();
  }
}
