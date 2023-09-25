import { Author } from "./author";
import { Borrower } from './borrower';
/**
 * Created by asdha on 3/14/2017.
 */
export class Book {

  isbn: string;
  title: string;
  cover: string;
  pages: string;
  available: boolean;
  authors: Author[];
  borrower: Borrower;
  concatenatedAuthors: string;
}


