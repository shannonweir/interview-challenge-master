import { TestBed } from '@angular/core/testing';

import { BusinessProcessService } from './business-process.service';

describe('BusinessProcessService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: BusinessProcessService = TestBed.get(BusinessProcessService);
    expect(service).toBeTruthy();
  });
});
