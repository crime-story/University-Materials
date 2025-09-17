import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'mailFormat'
})
export class MailFormatPipe implements PipeTransform {

  transform(value: string, ...args: unknown[]): string {
    let mailPrefix = value.indexOf('@');
    let result = value.slice(0, mailPrefix);
    return result;
  }
  
}
