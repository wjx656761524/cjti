import { TestBed } from '@angular/core/testing';

import { CrowdService } from './crowd.service';

describe('OrderMgrService', () => {
  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: CrowdService = TestBed.get(CrowdService);
    expect(service).toBeTruthy();
  });
});
