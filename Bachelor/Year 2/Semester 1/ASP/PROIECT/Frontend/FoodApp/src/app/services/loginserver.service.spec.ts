import { TestBed } from '@angular/core/testing';

import { LoginserverService } from './loginserver.service';

describe('LoginserverService', () => {
  let service: LoginserverService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(LoginserverService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
