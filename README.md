# FF Max Overlay Lab

Protótipo Android Kotlin para testar:
- permissão de sobreposição;
- painel flutuante arrastável;
- captura de tela autorizada pelo MediaProjection;
- pipeline de frames (a primeira versão apenas recebe e libera os frames).

## Limite desta build
Esta versão **não identifica inimigos automaticamente**, não lê memória do Free Fire e não fornece coordenadas de jogadores. O radar/identificador visual exige um modelo de visão computacional treinado e validado com imagens próprias, que será uma etapa separada.

## Gerar APK pelo GitHub Actions
1. Crie um repositório vazio no GitHub.
2. Envie todos os arquivos desta pasta para a raiz do repositório.
3. Abra **Actions** e execute **Build Android APK** (ou faça push na branch `main`).
4. Baixe o artefato `ffmax-overlay-debug`.
5. Extraia `app-debug.apk` e instale no Galaxy M23.

## Teste no celular
1. Abra o app.
2. Toque em **Autorizar sobreposição** e habilite a permissão.
3. Volte ao app e toque em **Iniciar captura + painel**.
4. Confirme a captura de tela do Android.
5. O painel flutuante deve aparecer; use **Parar serviço** para encerrar.

Requer Android 8.0 ou superior. Compilação configurada com JDK 17, Gradle 8.9 e Android Gradle Plugin 8.7.3.
