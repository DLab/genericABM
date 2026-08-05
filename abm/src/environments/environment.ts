// The file contents for the current environment will overwrite these during build.
// The build system defaults to the dev environment which uses `environment.ts`, but if you do
// `ng build --env=prod` then `environment.prod.ts` will be used instead.
// The list of which env maps to which file can be found in `angular-cli.json`.

export const environment = {
  production: false,
  googleMapsApiKey: '',
  customers:[
            {
              title: 'dlab / CASCADE',
              background: 'linear-gradient(#11829f,#072938,#000000)',
              backgroundColor: '#0d4356',
              backgroundPrimaryBtnColor: '#0d4356',
              favicon: 'assets/img/cienciavida_logo.png',
              logo: 'assets/img/logo2.png',
              sideNaveWidth: '280px',
              downloadGestionContentType: 'xlsx',
              extraccionShowDatosFactura: false,
                    }
  ],
  selectedCustomer: 0,
  backend: '' // Put your backend here
};
