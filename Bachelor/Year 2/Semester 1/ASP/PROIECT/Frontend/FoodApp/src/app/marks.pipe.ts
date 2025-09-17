import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'marks'
})
export class MarksPipe implements PipeTransform {

  transform(value: unknown, ...args: unknown[]): unknown {
    return value + ':';
  }

}
